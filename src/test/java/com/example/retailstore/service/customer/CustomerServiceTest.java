package com.example.retailstore.service.customer;

import com.example.retailstore.entity.Customer;
import com.example.retailstore.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    CustomerRepository customerRepository;
    @InjectMocks
    CustomerServiceImpl customerService;

    @Test
    void findCustomerByIdTest_Success(){
        when(customerRepository.findById(anyLong())).thenReturn(
                Optional.of(new Customer(1L, "Virat", "Kohli", "virat@gmail.com", false, LocalDate.now().minusYears(3), null)));
        Customer customer = customerService.findCustomerById(1L);
        assertEquals("Virat", customer.getFirstName());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void findCustomerByIdTest_Failure(){
        when(customerRepository.findById(anyLong())).thenReturn(
                Optional.empty());
        Customer customer = customerService.findCustomerById(1L);
        assertNull(customer);
        verify(customerRepository, times(1)).findById(anyLong());
    }

}