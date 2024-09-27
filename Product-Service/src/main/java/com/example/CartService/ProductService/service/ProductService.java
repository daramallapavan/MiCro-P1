package com.example.CartService.ProductService.service;

import com.example.CartService.ProductService.dto.ProductUpdateDto;
import com.example.CartService.ProductService.entity.Product;
import com.example.CartService.ProductService.dto.ProductDto;
import com.example.CartService.ProductService.exception.ProductException;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto productDto);

    List<Product> getAllProducts();

    List<Product> getAllProducts(String field);

    Product getProductByProductName(String productName);

    String updateProduct(ProductUpdateDto productUpdateDto);

    String delete();

    String deleteByProduct(String productName);


    Product getProductByProductId(Long productId) ;
}
