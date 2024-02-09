package com.example.retailstore.controller;

import com.example.retailstore.dto.DiscountResponse;
import com.example.retailstore.dto.Order;
import com.example.retailstore.service.discount.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartNetPayableController {

    private final DiscountService discountService;

    /**
     * Endpoint for calculating the net payable amount for an order.
     *
     * @param order The order for which to calculate the net payable amount.
     * @return A DiscountResponse object containing the total order amount and the net payable amount after applying discounts.
     */
    @PostMapping()
    public DiscountResponse calculateNetPayable(@RequestBody @Valid Order order){
        return discountService.calculateNetPayable(order);
    }

}
