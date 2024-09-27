package com.example.CartService.service;

import com.example.CartService.dto.AddToCartDto;
import com.example.CartService.dto.CartPriceDetails;
import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItems;
import com.example.CartService.exception.CartException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    String addToCart(AddToCartDto addToCartDto,Long userId) throws CartException;

    public String addToCartWithParams(String  productName, String email);

    public String addToCartWithParams(String  productName,int quantity, String email);

    Cart createCart(String email);


    Cart getCartByUserId(String email) throws CartException;

    void clearCartItems();

    String addToCartTo(AddToCartDto addToCartDto, String email);


    String clearCartByEmailId(String email);

    List<CartItems> listOfItems(String email);

    String deleteCartItem(Long cartItemId);

    CartPriceDetails priceDetails(String email);

    String addProductsToCart(Long productId, String email);
}
