package com.example.CartService.PaymentService.service;

import com.example.CartService.PaymentService.entity.Payment;

public interface PaymentService {
    String doPay(String orderNumber);

    Payment getPayment(String orderNumber);
}
