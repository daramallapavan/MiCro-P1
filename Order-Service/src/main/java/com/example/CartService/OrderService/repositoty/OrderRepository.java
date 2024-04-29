package com.example.CartService.OrderService.repositoty;

import com.example.CartService.OrderService.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Long> {



    Optional<Orders> findByOrderNumber(String orderNumber);

    List<Orders> findByEmail(String email);
}
