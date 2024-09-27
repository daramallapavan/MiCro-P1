package com.example.CartService.OrderService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class CartItems {


    private Long cartItemId;


    private Cart cart;


    private String productName;
    private Long userId;

    private int quantity;
    private double price;

    private String imageUrl;

    private double productTotalPrice;



}
