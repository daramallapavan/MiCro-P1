package com.example.CartService.ProductService.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProductDto {


    private String productName;


    private int quantity;


    private double price;

    private String imageUrl;
}
