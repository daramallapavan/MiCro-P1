package com.example.CartService.PaymentService.repository;

import com.example.CartService.PaymentService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderNumber(String orderNumber);
}
