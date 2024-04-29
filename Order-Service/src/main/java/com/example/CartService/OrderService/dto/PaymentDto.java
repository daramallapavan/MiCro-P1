package com.example.CartService.OrderService.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long id;

    private String paymentType;


    private double totalOrderAmount;

    private String paymentStatus;

    private String paymentNumber;


    private String orderNumber;

    private Long userId;

}
