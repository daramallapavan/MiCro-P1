package com.example.CartService.ProductService.service;

import com.example.CartService.ProductService.dto.ProductUpdateDto;
import com.example.CartService.ProductService.entity.Product;
import com.example.CartService.ProductService.dto.ProductDto;

import java.util.List;

public interface ProductService {
    String createProduct(ProductDto productDto);

    List<Product> getAllProducts();

    Product getProductByProductName(String productName);

    String updateProduct(ProductUpdateDto productUpdateDto);

    String delete();

    String deleteByProduct(String productName);
}
