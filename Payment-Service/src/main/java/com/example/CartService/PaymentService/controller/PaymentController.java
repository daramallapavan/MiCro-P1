package com.example.CartService.PaymentService.controller;

import com.example.CartService.PaymentService.entity.Payment;
import com.example.CartService.PaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    public String doPayment( @RequestParam String orderNumber){
       return paymentService.doPay(orderNumber);

    }
    @GetMapping("/getPayment")
    public Payment getPayment(@RequestParam String orderNumber){
        return paymentService.getPayment(orderNumber);
    }
}
