package com.example.CartService.repository;

import com.example.CartService.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    //Cart findByUserId(Long userId);

    Cart findByEmail(String email);
}
