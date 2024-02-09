package com.example.retailstore.service.discount;

import com.example.retailstore.dto.DiscountResponse;
import com.example.retailstore.dto.Order;
import com.example.retailstore.dto.PurchaseItem;
import com.example.retailstore.entity.AffiliateCustomer;
import com.example.retailstore.entity.Customer;
import com.example.retailstore.entity.Discount;
import com.example.retailstore.entity.ItemType;
import com.example.retailstore.repository.DiscountRepository;
import com.example.retailstore.service.ServiceConstants;
import com.example.retailstore.service.customer.CustomerService;
import com.example.retailstore.service.item.type.ItemTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    @Mock
    CustomerService customerService;
    @Mock
    ItemTypeService itemTypeService;
    @Mock
    DiscountRepository discountRepository;
    @InjectMocks
    DiscountServiceImpl discountService;

    Discount discountForEmp = new Discount(1L, "EMP", "Employee discount", new BigDecimal(30));
    Discount discountForAffUser = new Discount(2L, "AFF", "Affiliate discount", new BigDecimal(10));
    Discount discountForLongTermUser = new Discount(3L, "LTC", "Long term customer discount", new BigDecimal(5));
    Order order = new Order(4L, new HashSet<>(Arrays.asList(
            new PurchaseItem(1L, "Mobile", "ELECTRONICS", 1, new BigDecimal(1000)),
            new PurchaseItem(2L, "Oil", "GROCERY", 1, new BigDecimal(90))
    )));
    List<ItemType> itemTypeList = Arrays.asList(new ItemType(1L, "ELECTRONICS", "Electronic items", true),
            new ItemType(2L, "GROCERY", "Grocery items", false),
            new ItemType(3L, "TOYS", "Baby toys", true),
            new ItemType(4L, "APPAREL", "Clothes", true),
            new ItemType(5L, "FURNITURE", "Home furniture", true),
            new ItemType(6L, "SHOES", "Shoes and sandals", true));

    @Test
    void GivenUserIsNewToWebsite_CalculateNetPayable_OnlyFixedDiscountApplies(){
        Customer newCustomer = new Customer(4L, "Shubman", "Gill", "shubman@gmail.com", false,
                LocalDate.now().minusMonths(1), null);
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(1090), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(1040), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
    }

    @Test
    void GivenUserIsLongTerm_CalculateNetPayable_LongTermDiscountAndFixedDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Virat", "Kohli", "virat@gmail.com", false,
                LocalDate.now().minusYears(3), null);
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.LONG_TERM_CUSTOMER_DISCOUNT_CODE)).thenReturn(discountForLongTermUser);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(1090), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(990), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.LONG_TERM_CUSTOMER_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsAffiliate_CalculateNetPayable_AffiliateDiscountAndFixedDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Rohit", "Sharma", "rohit@gmail.com", false,
                LocalDate.now().minusYears(1),
                new AffiliateCustomer(1L, "Rohit"));
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.AFFILIATE_DISCOUNT_CODE)).thenReturn(discountForAffUser);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(1090), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(945), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.AFFILIATE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsEmployee_CalculateNetPayable_EmployeeDiscountAndFixedDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Surya", "Kumar", "surya@gmail.com", true,
                LocalDate.now().minusYears(1),
                null);
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE)).thenReturn(discountForEmp);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(1090), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(755), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsEmployeeOnlyGroceries_CalculateNetPayable_EmployeeDiscountAndFixedDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Surya", "Kumar", "surya@gmail.com", true,
                LocalDate.now().minusYears(1),
                null);
        Order order = new Order(4L, new HashSet<>(Arrays.asList(
                new PurchaseItem(1, "Sugar", "GROCERY", 1, new BigDecimal(50)),
                new PurchaseItem(2, "Oil", "GROCERY", 1, new BigDecimal(90))
        )));
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE)).thenReturn(discountForEmp);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(140), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(135), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsAffiliateAndEmployee_CalculateNetPayable_EmployeeDiscountAndFixedDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Rohit", "Sharma", "rohit@gmail.com", true,
                LocalDate.now().minusYears(1),
                new AffiliateCustomer(1L, "Rohit"));
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.AFFILIATE_DISCOUNT_CODE)).thenReturn(discountForAffUser);
        when(discountRepository.findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE)).thenReturn(discountForEmp);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(1090), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(755), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.AFFILIATE_DISCOUNT_CODE);
        verify(discountRepository, times(1)).findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsEmployeeAndCartValueLessThan100_CalculateNetPayable_OnlyEmployeeDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Surya", "Kumar", "surya@gmail.com", true,
                LocalDate.now().minusYears(1),
                null);
        Order order = new Order(4L, new HashSet<>(List.of(
                new PurchaseItem(1, "Ball", "TOYS", 1, new BigDecimal(50))
        )));
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE)).thenReturn(discountForEmp);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(50), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(35), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

    @Test
    void GivenUserIsEmployeeAndCartValueLessThan100ForGroceries_CalculateNetPayable_NoDiscountApplies(){
        Customer newCustomer = new Customer(1L, "Surya", "Kumar", "surya@gmail.com", true,
                LocalDate.now().minusYears(1),
                null);
        Order order = new Order(4L, new HashSet<>(List.of(
                new PurchaseItem(1, "Sugar", "GROCERY", 1, new BigDecimal(50))
        )));
        when(customerService.findCustomerById(anyLong())).thenReturn(newCustomer);
        when(discountRepository.findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE)).thenReturn(discountForEmp);
        when(itemTypeService.findAll()).thenReturn(itemTypeList);
        DiscountResponse discountResponse = discountService.calculateNetPayable(order);
        assertEquals(BigDecimal.valueOf(50), discountResponse.totalOrderAmount());
        assertEquals(BigDecimal.valueOf(50), discountResponse.netPayable());
        verify(customerService, times(1)).findCustomerById(anyLong());
        verify(discountRepository, times(1)).findByCode(ServiceConstants.EMPLOYEE_DISCOUNT_CODE);
        verify(itemTypeService, times(1)).findAll();
    }

}