package com.example.retailstore.controller;

import com.example.retailstore.controller.advice.ValidationExceptionHandler;
import com.example.retailstore.dto.DiscountResponse;
import com.example.retailstore.dto.Order;
import com.example.retailstore.dto.PurchaseItem;
import com.example.retailstore.service.discount.DiscountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartNetPayableControllerTest {
    @Mock
    DiscountService discountService;
    @InjectMocks
    CartNetPayableController cartNetPayableController;

    ObjectMapper objectMapper = new ObjectMapper();
    MockMvc mvc;

    @Test
    void findCardNetPayableTest_Success(){
        Order order = new Order(4L, new HashSet<>(Arrays.asList(
                new PurchaseItem(1, "Mobile", "ELECTRONICS", 1, BigDecimal.valueOf(1000)),
                new PurchaseItem(2, "Oil", "GROCERY", 1, BigDecimal.valueOf(90))
        )));
        DiscountResponse mockResponse = new DiscountResponse(BigDecimal.valueOf(1090), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(1040));
        when(discountService.calculateNetPayable(order)).thenReturn(mockResponse);
        DiscountResponse netPayable = cartNetPayableController.calculateNetPayable(order);
        assertEquals(mockResponse.totalOrderAmount(), netPayable.totalOrderAmount());
        verify(discountService, times(1)).calculateNetPayable(order);
    }

    @Test
    void findCardNetPayableTest_Success_Ok() throws Exception {
        Order order = new Order(4L, new HashSet<>(Arrays.asList(
                new PurchaseItem(1, "Mobile", "ELECTRONICS", 1, BigDecimal.valueOf(1000)),
                new PurchaseItem(2, "Oil", "GROCERY", 1, BigDecimal.valueOf(90))
        )));

        mvc = MockMvcBuilders.standaloneSetup(cartNetPayableController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
        mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order))).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void givenCustomerIdNull_FindCardNetPayableTest_Failed_BadRequest() throws Exception {
        Order order = new Order(null, new HashSet<>(Arrays.asList(
                new PurchaseItem(1, "Mobile", "ELECTRONICS", 1, BigDecimal.valueOf(1000)),
                new PurchaseItem(2, "Oil", "GROCERY", 1, BigDecimal.valueOf(90))
        )));
        String errorResponse = """
                {"validationErrors":[{"errorMessage":"Customer ID cannot be null","target":"customerId"}],"error":"Validation Failed"}""";
        mvc = MockMvcBuilders.standaloneSetup(cartNetPayableController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
        MockHttpServletResponse response = mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order))).andReturn().getResponse();
        assertEquals(400, response.getStatus());
        assertEquals(errorResponse, response.getContentAsString());
    }

    @Test
    void givenPurchaseItemsNull_FindCardNetPayableTest_Failed_BadRequest() throws Exception {
        Order order = new Order(4L, null);
        String errorResponse = """
                {"validationErrors":[{"errorMessage":"Purchase Items cannot be null","target":"purchaseItems"}],"error":"Validation Failed"}""";
        mvc = MockMvcBuilders.standaloneSetup(cartNetPayableController)
                .setControllerAdvice(new ValidationExceptionHandler())
                .build();
        MockHttpServletResponse response = mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order))).andReturn().getResponse();
        assertEquals(400, response.getStatus());
        assertEquals(errorResponse, response.getContentAsString());
    }

}