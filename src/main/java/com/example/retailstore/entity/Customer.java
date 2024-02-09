package com.example.retailstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isEmployee;
    private LocalDate createdDate;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "affiliateId", referencedColumnName = "id")
    private AffiliateCustomer affiliateCustomer;

    public Customer(Long id, String firstName, String lastName, String email,
                    boolean isEmployee, LocalDate createdDate,
                    AffiliateCustomer affiliateCustomer) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEmployee = isEmployee;
        this.createdDate = createdDate;
        this.affiliateCustomer = affiliateCustomer;
    }
}
