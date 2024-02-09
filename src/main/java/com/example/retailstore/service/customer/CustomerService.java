package com.example.retailstore.service.customer;

import com.example.retailstore.entity.Customer;

public interface CustomerService {
    Customer findCustomerById(Long customerId);
}
