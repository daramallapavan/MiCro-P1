package com.example.CartService.PaymentService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> productException(PaymentException paymentException){
        return new ResponseEntity<>( paymentException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
