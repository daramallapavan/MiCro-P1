package com.example.CartService.OrderService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String city;

    private String pinCode;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String landMark;

    private String email;

    @OneToOne
    @JoinColumn(name = "order_id",nullable = false)
    @JsonIgnore
    private Orders orders;




}
