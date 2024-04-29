package com.example.CartService.StockService.repository;

import com.example.CartService.StockService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {




    Optional<User> findByEmail(String email);
}
