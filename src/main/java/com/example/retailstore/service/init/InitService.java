package com.example.retailstore.service.init;

import com.example.retailstore.entity.AffiliateCustomer;
import com.example.retailstore.entity.Customer;
import com.example.retailstore.entity.Discount;
import com.example.retailstore.entity.ItemType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.retailstore.repository.AffiliateCustomerRepository;
import com.example.retailstore.repository.CustomerRepository;
import com.example.retailstore.repository.DiscountRepository;
import com.example.retailstore.repository.ItemTypeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitService {

    private final CustomerRepository customerRepository;
    private final AffiliateCustomerRepository affiliateCustomerRepository;
    private final DiscountRepository discountRepository;
    private final ItemTypeRepository itemTypeRepository;

    /**
     * Initializes the data for the application, including discounts, item types, affiliate customers, and regular customers.
     * This method is annotated with @PostConstruct to ensure it is executed after the bean is instantiated.
     */
    @PostConstruct
    public void init(){
        log.info("Initiating the data...");
        discountRepository.saveAll(Arrays.asList(new Discount(1L, "EMP", "Employee discount", BigDecimal.valueOf(30)),
                new Discount(2L, "AFF", "Affiliate discount", BigDecimal.valueOf(10)),
                new Discount(3L, "LTC", "Long term customer discount", BigDecimal.valueOf(5))));
        itemTypeRepository.saveAll(Arrays.asList(new ItemType(1L, "ELECTRONICS", "Electronic items", true),
                new ItemType(2L, "GROCERY", "Grocery items", false),
                new ItemType(3L, "TOYS", "Baby toys", true),
                new ItemType(4L, "APPAREL", "Clothes", true),
                new ItemType(5L, "FURNITURE", "Home furniture", true),
                new ItemType(6L, "SHOES", "Shoes and sandals", true)));
        AffiliateCustomer affiliateCustomer = affiliateCustomerRepository.save(new AffiliateCustomer(1L, "Sharma affiliate team"));
        customerRepository.saveAll(Arrays.asList(new Customer(1L, "Virat", "Kohli", "virat@gmail.com", false, LocalDate.now().minusYears(3), null),
                new Customer(2L, "Rohit", "Sharma", "rohit@gmail.com", false, LocalDate.now().minusYears(3), affiliateCustomer),
                new Customer(3L, "Surya", "Yadav", "surya@gmail.com", true, LocalDate.now().minusYears(1), null),
                new Customer(4L, "Shubman", "Gill", "shubman@gmail.com", false, LocalDate.now().minusMonths(1), null)
                ));
        log.info("Initiating the data is successfully completed");
    }
}
