package com.example.CartService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartDtoResponse {
    private String response;

    private boolean responseTypeStatus;
}
