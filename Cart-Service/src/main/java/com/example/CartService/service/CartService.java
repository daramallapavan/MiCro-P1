package com.example.CartService.service;

import com.example.CartService.dto.AddToCartDto;
import com.example.CartService.entity.Cart;
import com.example.CartService.exception.CartException;

public interface CartService {
    String addToCart(AddToCartDto addToCartDto,Long userId) throws CartException;

    Cart createCart(String email);


    Cart getCartByUserId(String email) throws CartException;

    void clearCartItems();

    String addToCartTo(AddToCartDto addToCartDto, String email);


    String clearCartByEmailId(String email);
}
