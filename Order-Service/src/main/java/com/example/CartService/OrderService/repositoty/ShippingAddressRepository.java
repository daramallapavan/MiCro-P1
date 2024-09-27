package com.example.CartService.OrderService.repositoty;

import com.example.CartService.OrderService.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingAddressRepository  extends JpaRepository<ShippingAddress,Long> {
    List<ShippingAddress> findByEmail(String email);
}
