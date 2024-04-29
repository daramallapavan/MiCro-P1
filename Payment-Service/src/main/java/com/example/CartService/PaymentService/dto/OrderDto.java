package com.example.CartService.PaymentService.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {


    private String orderNumber;


    private List<OrdersItems> ordersItemsList;

    private String status;

    private double totalOrderPrice;

    private String email;
    private String paymentStatus;
}
