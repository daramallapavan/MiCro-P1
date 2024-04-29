package com.example.CartService.OrderService.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
public class Cart {


    private Long cartId;


    private List<CartItems> cartItems;

    private Long userId;






}
