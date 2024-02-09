package com.example.retailstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class AffiliateCustomer extends BaseEntity {
    private String name;

    public AffiliateCustomer(Long id, String name) {
        super(id);
        this.name = name;
    }
}
