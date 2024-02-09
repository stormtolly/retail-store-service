package com.example.retailstore.dto;

import java.math.BigDecimal;

public record DiscountResponse(BigDecimal totalOrderAmount,
                               BigDecimal discountPercentage,
                               BigDecimal discountPercentageAmount,
                               BigDecimal fixedDiscountAmount,
                               BigDecimal netPayable) {
}
