package com.example.retailstore.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
public class Discount extends BaseEntity {
    private String code;
    private String description;
    private BigDecimal discountPercentageValue;

    public Discount(Long id, String code, String description, BigDecimal discountPercentageValue) {
        super(id);
        this.code = code;
        this.description = description;
        this.discountPercentageValue = discountPercentageValue;
    }

}
