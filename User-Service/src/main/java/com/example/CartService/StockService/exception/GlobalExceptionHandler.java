package com.example.CartService.StockService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> exceptionHandler(UserNotFoundException userNotFoundException){
        return new ResponseEntity<>( userNotFoundException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
