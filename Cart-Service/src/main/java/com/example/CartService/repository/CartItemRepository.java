package com.example.CartService.repository;

import com.example.CartService.dto.ProductDto;
import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItems,Long> {


//    @Query("SELECT ci From CartItem ci Where ci.cart=:cart And ci.product=:product And ci.userId=:userId")
//    public CartItems isCartItemExist(@Param("cart") Cart cart,
//                                    @Param("product") ProductDto product,
//                                    @Param("userId") Long userId
//    );


/*
    @Query("SELECT c From CartItems c Where c.cart=:cart And c.userId=:userId And productName=:productName")
    public CartItems isCartItemsExists(@Param("cart") Cart cart,
                                     @Param( "userId" ) Long userId,
                                     @Param( "productName" ) String productName);
*/

    @Query("SELECT c From CartItems c Where c.cart=:cart And c.email=:email And productName=:productName")
    public CartItems isCartItemsExists(@Param("cart") Cart cart,
                                       @Param( "email" ) String email,
                                       @Param( "productName" ) String productName);
    List<CartItems> findByEmail(String email);



    CartItems findByCartItemId(Long cartItemId);
}
