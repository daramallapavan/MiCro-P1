package com.example.CartService.OrderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<?> productException(OrderException orderException){
        return new ResponseEntity<>( orderException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
