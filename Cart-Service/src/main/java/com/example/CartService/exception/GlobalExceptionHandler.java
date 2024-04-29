package com.example.CartService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartException.class)
    public ResponseEntity<?> productException(CartException cartException){
        return new ResponseEntity<>( cartException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
