package com.example.CartService.StockService.controller;

import com.example.CartService.StockService.dto.UseRegisterResponse;
import com.example.CartService.StockService.dto.UserRequest;
import com.example.CartService.StockService.entity.User;
import com.example.CartService.StockService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UseRegisterResponse> userCreation(@RequestBody User user){
        String response = userService.createUser( user );
        UseRegisterResponse useRegisterResponse=new UseRegisterResponse();
        useRegisterResponse.setResponse( response );
        return new ResponseEntity<>(  useRegisterResponse,HttpStatus.OK);
    }

    @PostMapping("/login")
    public String validateUser(@RequestBody UserRequest userRequest){
       return userService.loginAndGetToken( userRequest );
    }

    @PostMapping("/loginEasy")
    public User validateUserWithoutToken(@RequestBody UserRequest userRequest){
        return userService.validateLoginGetUserObject(userRequest);
    }



    @GetMapping("/validToken")
    public String validToken(@RequestParam String token){
         userService.validToken(token);
         return "Valid Token";
    }
}
