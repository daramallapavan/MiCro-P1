package com.example.CartService.PaymentService.service;

import com.example.CartService.PaymentService.entity.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {
    String doPay(String orderNumber);


    Payment getPayment(String orderNumber);
}
