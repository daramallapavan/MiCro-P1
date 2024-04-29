package com.example.CartService.StockService.service;

import com.example.CartService.StockService.dto.UserRequest;
import com.example.CartService.StockService.entity.User;

public interface UserService {
    String createUser(User user);

    String loginAndGetToken(UserRequest userRequest);


    
    
    void validToken(String token);


}
