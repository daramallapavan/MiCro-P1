package com.example.CartService.controller;

import com.example.CartService.config.CartServiceConfig;
import com.example.CartService.dto.*;
import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItems;
import com.example.CartService.exception.CartException;
import com.example.CartService.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
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
    public String cart(@RequestParam String email){
        cartService.createCart( email );
        return "cart is created";
    }

    @DeleteMapping("/deleteCartItem")
    public ResponseEntity<DeleteCartItemResponse> deleteCartItem(@RequestParam Long cartItemId){
        String s = cartService.deleteCartItem( cartItemId );
        return new ResponseEntity<>(  new DeleteCartItemResponse(s),HttpStatus.OK );
    }


    @GetMapping("/getCart")
    public Cart getCartByUserId(@RequestHeader("email") String email) throws CartException {
        return cartService.getCartByUserId(email);
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestBody AddToCartDto addToCartDto,@RequestParam Long userId) throws CartException {
        return cartService.addToCart(addToCartDto,userId);
    }
    @PostMapping("/addToCartNew")
    public String addToCartNew(@RequestBody AddToCartDto addToCartDto,@RequestHeader("email") String email)  {
        return cartService.addToCartTo(addToCartDto,email);
    }

    @PostMapping(name = "/addProductToCart/{productId}")
    public String addToCartNew(@PathVariable Long productId,
                               @RequestHeader("email") String email)  {
        return cartService.addProductsToCart(productId,email);
    }
    @PostMapping("/addToCartParam")
    public ResponseEntity<AddToCartDtoResponse> addToCartParams(@RequestParam String productName,
                                                                @RequestHeader("email") String email)  {
        String response = cartService.addToCartWithParams( productName, email );

        AddToCartDtoResponse build = AddToCartDtoResponse.builder()
                .response( response )
                .responseTypeStatus( true )
                .build();
        return new ResponseEntity<>( build, HttpStatus.CREATED );
    }


    @PostMapping("/addToCartParamWithQuantity")
    public ResponseEntity<AddToCartDtoResponse> addToCartParamsWithQuantity(@RequestParam String productName,
                                                                            @RequestHeader("email") String email,
                                                                            @RequestParam int quantity)  {
        String response = cartService.addToCartWithParams( productName,quantity,email );

        AddToCartDtoResponse build = AddToCartDtoResponse.builder()
                .response( response )
                .responseTypeStatus( true )
                .build();
        return new ResponseEntity<>( build, HttpStatus.CREATED );
    }
    @DeleteMapping("/deleteCartItems")
    public String clearCartItems(){
        cartService.clearCartItems();

        return "cartItems deleted successfully";
    }

    @DeleteMapping("/clear")
    public String clearCartItemsByEmail(@RequestHeader("email") String email){
        return cartService.clearCartByEmailId( email );

    }

    @GetMapping("/cartItems")
    public List<CartItems> listOfCartItems(@RequestHeader("email") String email){
        return cartService.listOfItems(email);
    }


    @GetMapping("/priceDetails")
    public CartPriceDetails priceDetails(@RequestParam String email){
      return   cartService.priceDetails( email );
    }

}
