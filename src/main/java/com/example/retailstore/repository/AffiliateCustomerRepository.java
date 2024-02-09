package com.example.retailstore.repository;

import com.example.retailstore.entity.AffiliateCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateCustomerRepository extends JpaRepository<AffiliateCustomer, Long> {
}
