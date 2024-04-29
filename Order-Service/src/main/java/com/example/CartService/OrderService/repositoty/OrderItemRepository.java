package com.example.CartService.OrderService.repositoty;

import com.example.CartService.OrderService.entity.OrdersItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrdersItems,Long> {
}
