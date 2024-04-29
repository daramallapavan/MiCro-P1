package com.example.CartService.ProductService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<?> productException(ProductException productException){
        return new ResponseEntity<>( productException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
