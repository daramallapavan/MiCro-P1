package com.example.CartService.OrderService.repositoty;

import com.example.CartService.OrderService.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository  extends JpaRepository<ShippingAddress,Long> {
}
