package com.example.CartService.StockService.controller;

import com.example.CartService.StockService.dto.UserRequest;
import com.example.CartService.StockService.entity.User;
import com.example.CartService.StockService.service.UserService;
import com.example.CartService.StockService.serviceImpl.JavaEmailService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

/*    @Autowired
    private JavaEmailService javaEmailService;




    @PostMapping("/send")
    public String sendEmail(){
        String to = "godclutch732@gmail.com";
        String subject = "Test Email";
        String text = "This is a test email sent using Java Spring Boot!";
        javaEmailService.simpleMimeMessage( to,subject,text );
        return "email sent to successfully " ;
    }*/

    @PostMapping("/register")
    public String userCreation(@RequestBody User user){
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public String validateUser(@RequestBody UserRequest userRequest){
       return userService.loginAndGetToken( userRequest );
    }



    @GetMapping("/validToken")
    public String validToken(@RequestParam String token){
         userService.validToken(token);
         return "Valid Token";
    }
}
