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
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public Product createProduct(@RequestBody ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @GetMapping("/productList")
    public List<Product> listOfProducts()  {

      //  System.out.println("email from token................ "+email);
        return productService.getAllProducts();
    }

    @GetMapping("/productListSort")
    public List<Product> listOfProducts(@RequestParam String field)  {

        //  System.out.println("email from token................ "+email);
        return productService.getAllProducts(field);
    }


    @GetMapping("/getProduct")
    public Product getProductByProductName(@RequestParam String productName){
        return productService.getProductByProductName(productName);
    }

    @GetMapping("/getProductById/{productId}")
    public Product getProductByProductName(@PathVariable Long productId){
        return productService.getProductByProductId(productId);
    }
    @PutMapping("/update")
    public String updateProduct(@RequestBody ProductUpdateDto productUpdateDto){
        return productService.updateProduct(productUpdateDto);
    }
    @DeleteMapping("/delete")
    public String delete(){
        return productService.delete();
    }

    @DeleteMapping("/deleteByProductName")
    public String delete(@RequestParam String productName){
        return productService.deleteByProduct(productName);
    }
}
