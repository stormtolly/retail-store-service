package com.example.retailstore.service.customer;

import com.example.retailstore.entity.Customer;
import com.example.retailstore.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Finds a customer by their unique identifier.
     *
     * @param customerId unique identifier
     * @return Customer
     */
    @Override
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

}
