package com.example.retailstore.service.discount;

import com.example.retailstore.dto.DiscountResponse;
import com.example.retailstore.dto.Order;

public interface DiscountService {
    DiscountResponse calculateNetPayable(Order order);
}
