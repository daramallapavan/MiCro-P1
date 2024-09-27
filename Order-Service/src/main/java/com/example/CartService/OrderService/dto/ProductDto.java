package com.example.CartService.OrderService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
public class ProductDto {

    private Long productId;


    private String productName;


    private int quantity;


    private double price;

    private String imageUrl;

}
