package com.example.retailstore.repository;

import com.example.retailstore.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Discount findByCode(String discountCode);
}
