package com.example.CartService.ProductService.repository;

import com.example.CartService.ProductService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
   Optional<Product> findByProductName(String productName);

    void deleteByProductName(String productName);
}
