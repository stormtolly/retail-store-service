package com.example.retailstore.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ItemType extends BaseEntity {
    private String code;
    private String description;
    private boolean isDiscountApplicable;

    public ItemType(Long id, String code, String description, boolean isDiscountApplicable) {
        super(id);
        this.code = code;
        this.description = description;
        this.isDiscountApplicable = isDiscountApplicable;
    }
}
