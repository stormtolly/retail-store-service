package com.example.retailstore.dto;

import java.math.BigDecimal;

public record PurchaseItem(long itemId,
                           String itemName,
                           String itemTypeCode,
                           int quantity,
                           BigDecimal price) {
}
