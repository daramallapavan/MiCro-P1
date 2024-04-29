package com.example.CartService.PaymentService.dto;

import lombok.Data;

@Data
public class OrdersItems {

    private OrderDto orders;

    private String productName;

    private int quantity;

    private double price;

    private double itemTotalPrice;

    private Long userId;

}
