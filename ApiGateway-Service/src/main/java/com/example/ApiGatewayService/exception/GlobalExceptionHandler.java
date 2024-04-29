package com.example.ApiGatewayService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomGatewayException.class)
    public ResponseEntity<?> handleGatewayException(CustomGatewayException gatewayException){
        return new ResponseEntity<>( gatewayException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
