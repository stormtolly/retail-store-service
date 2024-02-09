package com.example.retailstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class AffiliateCustomer extends BaseEntity {
    private String name;

    public AffiliateCustomer(Long id, String name) {
        super(id);
        this.name = name;
    }
}
