package com.example.CartService.serviceImpl;

import com.example.CartService.config.CartServiceConfig;
import com.example.CartService.dto.AddToCartDto;
import com.example.CartService.dto.ProductDto;
import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItems;
import com.example.CartService.exception.CartException;
import com.example.CartService.repository.CartItemRepository;
import com.example.CartService.repository.CartRepository;
import com.example.CartService.service.CartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private CartItemRepository cartItemRepository;



    @Autowired
    private CartServiceConfig cartServiceConfig;
    @Override
    public String addToCart(AddToCartDto addToCartDto,Long userId) throws CartException {


/*

        ProductDto product = cartServiceConfig.getProductByProductName( addToCartDto.getProductName() );


        if(product.getProductName().equals( "default" )){

            throw new CartException( "Product server is down ,Please try later" );
        }


        Cart cart = cartRepository.findByUserId( userId );

        CartItems cartItemsExists = cartItemRepository.isCartItemsExists( cart, userId, addToCartDto.getProductName() );


        if (cartItemsExists==null){
            CartItems cartItems=new CartItems();
            cartItems.setCart( cart );
            cartItems.setProductName( product.getProductName() );
            cartItems.setUserId( userId );
            cartItems.setPrice( product.getPrice() );
            cartItems.setQuantity( addToCartDto.getQuantity() );
            cartItems.setProductTotalPrice(product.getPrice()*addToCartDto.getQuantity() );
            CartItems savedCartItem = cartItemRepository.save( cartItems );


            cart.getCartItems().add( savedCartItem );

            return "product added successfully to the cart";
        }
        try {
            throw new CartException("item already exist in cart");
        } catch (CartException e) {
            throw new RuntimeException( e );
        }
*/
    return null;


    }

    @Override
    public Cart createCart(String email) {

        Cart cart=new Cart();
        cart.setEmail( email );

        return cartRepository.save( cart );
    }

    @Override
    public Cart getCartByUserId(String email) throws CartException {
        Cart cart= cartRepository.findByEmail( email );
        if (cart.getCartItems().isEmpty()){

                throw new CartException( "no items present in the cart" );

        }
        return cart;
    }

    @Override
    public void clearCartItems() {


        cartItemRepository.deleteAll();
    }

    @SneakyThrows
    @Override
    public String addToCartTo(AddToCartDto addToCartDto, String email) {

        ProductDto product = cartServiceConfig.getProductByProductName( addToCartDto.getProductName() );

        if (addToCartDto.getQuantity()> product.getQuantity()){
            throw new CartException( "no more quantity for this product, total available quantity : "+product.getQuantity() );
        }

        if(product.getProductName().equals( "default" )){

            throw new CartException( "Product server is down ,Please try later" );
        }


        Cart cart = cartRepository.findByEmail(email  );

        CartItems cartItemsExists = cartItemRepository.isCartItemsExists( cart, email, addToCartDto.getProductName() );


        if (cartItemsExists==null){
            CartItems cartItems=new CartItems();
            cartItems.setCart( cart );
            cartItems.setProductName( product.getProductName() );
            cartItems.setEmail( email );
            cartItems.setPrice( product.getPrice() );
            cartItems.setQuantity( addToCartDto.getQuantity() );
            cartItems.setProductTotalPrice(product.getPrice()*addToCartDto.getQuantity() );
            CartItems savedCartItem = cartItemRepository.save( cartItems );


            cart.getCartItems().add( savedCartItem );

            return "product added successfully to the cart";
        }
        try {
            throw new CartException("item already exist in cart");
        } catch (CartException e) {
            throw new RuntimeException( e );
        }



    }

    @Override
    public String clearCartByEmailId(String email) {

        List<CartItems> cartItemsList = cartItemRepository.findByEmail( email );

        for (CartItems cartItem:cartItemsList){
            cartItemRepository.delete( cartItem );
        }



        return "clear cart items ,email "+email;
    }


}
