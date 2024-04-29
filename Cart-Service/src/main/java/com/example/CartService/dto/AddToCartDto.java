package com.example.CartService.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AddToCartDto {


    private String productName;
    private int quantity;

}
