package com.example.CartService.StockService.cartservice;

import com.example.CartService.StockService.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CartServiceApi {

    @Autowired
    private RestTemplate restTemplate;

    public  String createCart(String email){


        try{
           return restTemplate.
                    postForObject( "http://CART-SERVICE/cart/create?email=" + email, null, String.class );
        }catch (Exception httpServerErrorException){
            throw new UserNotFoundException( httpServerErrorException.getMessage() );
        }

    }

}
