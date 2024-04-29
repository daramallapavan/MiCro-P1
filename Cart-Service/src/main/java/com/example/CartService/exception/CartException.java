package com.example.CartService.exception;

public class CartException extends Throwable {
    public CartException(String itemAlreadyExistInCart) {
        super(itemAlreadyExistInCart);
    }
}
