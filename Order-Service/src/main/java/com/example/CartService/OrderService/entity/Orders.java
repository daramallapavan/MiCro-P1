package com.example.CartService.OrderService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    private List<OrdersItems> ordersItemsList;

    private String status;

    private double totalOrderPrice;

    private int totalItems;

    private String email;

    private String paymentStatus;

    private LocalDateTime createdAt;


    private String deliveryBy;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;





}
