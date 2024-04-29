package com.example.CartService.controller;

import com.example.CartService.config.CartServiceConfig;
import com.example.CartService.dto.AddToCartDto;
import com.example.CartService.dto.ProductDto;
import com.example.CartService.entity.Cart;
import com.example.CartService.exception.CartException;
import com.example.CartService.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartServiceConfig cartServiceConfig;

    @GetMapping("/getData")
    public ProductDto getData(@RequestParam String productName){
        return   cartServiceConfig.getProductByProductName( productName );

    }

    @PostMapping("/create")
    public String cart(@RequestHeader("email") String email){
        cartService.createCart( email );
        return "cart is created";
    }


    @GetMapping("/getCart")
    public Cart getCartByUserId(@RequestParam String email) throws CartException {
        return cartService.getCartByUserId(email);
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestBody AddToCartDto addToCartDto,@RequestParam Long userId) throws CartException {
        return cartService.addToCart(addToCartDto,userId);
    }
    @PostMapping("/addToCartNew")
    public String addToCartNew(@RequestBody AddToCartDto addToCartDto,@RequestParam String email)  {
        return cartService.addToCartTo(addToCartDto,email);
    }


    @DeleteMapping("/deleteCartItems")
    public String clearCartItems(){
        cartService.clearCartItems();

        return "cartItems deleted successfully";
    }

    @DeleteMapping("/clear")
    public String clearCartItemsByEmail(@RequestParam String email){
        return cartService.clearCartByEmailId( email );

    }



}
