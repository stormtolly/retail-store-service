package com.example.retailstore.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record Order(@NotNull(message = "Customer ID cannot be null") Long customerId,
                    @NotNull(message = "Purchase Items cannot be null") Set<PurchaseItem> purchaseItems) {
}
