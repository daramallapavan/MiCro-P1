package com.example.CartService.ProductService.controller;

import com.example.CartService.ProductService.dto.ProductUpdateDto;
import com.example.CartService.ProductService.entity.Product;
import com.example.CartService.ProductService.dto.ProductDto;
import com.example.CartService.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public String createProduct(@RequestBody ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @GetMapping("/productList")
    public List<Product> listOfProducts()  {

      //  System.out.println("email from token................ "+email);
        return productService.getAllProducts();
    }


    @GetMapping("/getProduct")
    public Product getProductByProductName(@RequestParam String productName){
        return productService.getProductByProductName(productName);
    }

    @PutMapping("/update")
    public String updateProduct(@RequestBody ProductUpdateDto productUpdateDto){
        return productService.updateProduct(productUpdateDto);
    }
}
