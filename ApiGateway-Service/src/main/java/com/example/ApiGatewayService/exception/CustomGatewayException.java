package com.example.ApiGatewayService.exception;

public class CustomGatewayException extends RuntimeException{

    public CustomGatewayException(String msg){
        super(msg);
    }
}
