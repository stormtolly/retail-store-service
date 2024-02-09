package com.example.retailstore.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class ServiceConstants {
    public static final String EMPLOYEE_DISCOUNT_CODE = "EMP";
    public static final String AFFILIATE_DISCOUNT_CODE = "AFF";
    public static final String LONG_TERM_CUSTOMER_DISCOUNT_CODE = "LTC";
    public static final BigDecimal TARGET_VALUE = new BigDecimal(100);
    public static final BigDecimal FIXED_DISCOUNT_VALUE = new BigDecimal(5);
    public static final long TWO_YEARS = 2L;

}