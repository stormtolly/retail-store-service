package com.example.retailstore.service.discount;

import com.example.retailstore.dto.DiscountResponse;
import com.example.retailstore.dto.Order;
import com.example.retailstore.dto.PurchaseItem;
import com.example.retailstore.entity.Customer;
import com.example.retailstore.entity.Discount;
import com.example.retailstore.entity.ItemType;
import com.example.retailstore.repository.DiscountRepository;
import com.example.retailstore.service.ServiceConstants;
import com.example.retailstore.service.customer.CustomerService;
import com.example.retailstore.service.item.type.ItemTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountServiceImpl implements DiscountService {

    private final CustomerService customerService;
    private final ItemTypeService itemTypeService;
    private final DiscountRepository discountRepository;

    /**
     * Calculates the net payable amount for an order, taking into account discounts based on customer type and total order amount.
     *
     * @param order The order for which to calculate the net payable amount.
     * @return A DiscountResponse object containing the total order amount and the net payable amount after applying discounts.
     */
    @Override
    public DiscountResponse calculateNetPayable(Order order) {
        log.debug("Calculating net payable amount for order {}", order.customerId());
        Customer customer = customerService.findCustomerById(order.customerId());
        BigDecimal discountPercentage = calculateApplicableDiscountForCustomer(customer);
        BigDecimal totalOrderAmount = order.purchaseItems().stream().map(PurchaseItem::price).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Total order amount for order {}: {}", order.customerId(), totalOrderAmount);
        BigDecimal percentageDiscountAmount = BigDecimal.ZERO;
        if(discountPercentage.intValue() > 0)
            percentageDiscountAmount = calculatePercentageDiscountAmount(order.purchaseItems(), discountPercentage);
        log.debug("Percentage discount amount for order {}: {}", order.customerId(), percentageDiscountAmount);
        BigDecimal totalAfterPercentageDiscount = totalOrderAmount.subtract(percentageDiscountAmount);
        BigDecimal fixedDiscountAmount = totalAfterPercentageDiscount.divideToIntegralValue(ServiceConstants.TARGET_VALUE).
                multiply(ServiceConstants.FIXED_DISCOUNT_VALUE);
        log.debug("Fixed discount amount for order {}: {}", order.customerId(), fixedDiscountAmount);
        BigDecimal netPayable = totalAfterPercentageDiscount.subtract(fixedDiscountAmount);
        log.debug("Net payable amount for order {}: {}", order.customerId(), netPayable);
        return new DiscountResponse(totalOrderAmount, discountPercentage, percentageDiscountAmount,
                fixedDiscountAmount, netPayable);
    }

    /**
     * Calculates the total discount amount based on the percentage discount applicable to eligible items in the purchase.
     *
     * @param purchaseItems      The set of purchase items for which to calculate the discount.
     * @param discountPercentage The percentage of discount applicable to eligible items.
     * @return The total discount amount based on the percentage discount.
     */
    private BigDecimal calculatePercentageDiscountAmount(Set<PurchaseItem> purchaseItems, BigDecimal discountPercentage) {
        Map<String, Boolean> itemTypes = itemTypeService.findAll().stream().collect(Collectors.toMap(ItemType::getCode, ItemType::isDiscountApplicable));
        BigDecimal totalOfItemsEligibleForDiscount = purchaseItems.stream().filter(item -> itemTypes.get(item.itemTypeCode())).map(PurchaseItem::price).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalOfItemsEligibleForDiscount.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
    }

    /**
     * Calculates the applicable discount percentage for a given customer based on their attributes such as employee status, affiliate status, and longevity as a customer.
     *
     * @param customer The customer for whom to calculate the applicable discount.
     * @return The applicable discount percentage based on the customer's attributes.
     */
    private BigDecimal calculateApplicableDiscountForCustomer(Customer customer) {
        Set<String> discountCode = new HashSet<>();
        if (customer.isEmployee()) {
            discountCode.add(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        }
        if (Optional.ofNullable(customer.getAffiliateCustomer()).isPresent()) {
            discountCode.add(ServiceConstants.AFFILIATE_DISCOUNT_CODE);
        }
        if (customer.getCreatedDate().isBefore(LocalDate.now().minusYears(ServiceConstants.TWO_YEARS))){
            discountCode.add(ServiceConstants.LONG_TERM_CUSTOMER_DISCOUNT_CODE);
        }
        Optional<Discount> maxDiscount = discountCode.stream().map(discountRepository::findByCode).
                max(Comparator.comparing(Discount::getDiscountPercentageValue));
        return maxDiscount.isPresent() ? maxDiscount.get().getDiscountPercentageValue() : BigDecimal.ZERO;
    }
}
