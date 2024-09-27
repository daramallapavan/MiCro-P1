package com.example.CartService.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressResponseDto {

    private String name;

    private String city;

    private String pinCode;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String landMark;

}
