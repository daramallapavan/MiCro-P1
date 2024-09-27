package com.example.CartService.serviceImpl;

import com.example.CartService.config.CartServiceConfig;
import com.example.CartService.config.ProductServiceApi;
import com.example.CartService.dto.AddToCartDto;
import com.example.CartService.dto.CartPriceDetails;
import com.example.CartService.dto.ProductDto;
import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItems;
import com.example.CartService.exception.CartException;
import com.example.CartService.repository.CartItemRepository;
import com.example.CartService.repository.CartRepository;
import com.example.CartService.service.CartService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductServiceApi productServiceApi;

    @Autowired
    private CartItemRepository cartItemRepository;


    private Cache<String,List<CartItems>> listCartItemsCache;

    private Cache<String,CartItems> cartItemsCache;

    @PostConstruct
    private void init(){
        listCartItemsCache=  Caffeine.newBuilder()
                .maximumSize( 100 )
                .build();

        cartItemsCache=  Caffeine.newBuilder()
                .maximumSize( 100 )
                .build();

    }

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

    @SneakyThrows
    @Override
    public String addToCartWithParams(String  productName, String email) {
        int quantity=1;

        ProductDto product = cartServiceConfig.getProductByProductName( productName );

        if (quantity> product.getQuantity()){
            throw new CartException( "no more quantity for this product, total available quantity : "+product.getQuantity() );
        }

        if(product.getProductName().equals( "default" )){

            throw new CartException( "Product server is down ,Please try later" );
        }


        Cart cart = cartRepository.findByEmail(email  );

        CartItems cartItemsExists = cartItemRepository.isCartItemsExists( cart, email, productName );


        if (cartItemsExists==null){
            CartItems cartItems=new CartItems();
            cartItems.setCart( cart );
            cartItems.setProductName( product.getProductName() );
            cartItems.setEmail( email );
            cartItems.setImageUrl( product.getImageUrl() );
            cartItems.setPrice( product.getPrice() );
            cartItems.setQuantity( quantity);
            cartItems.setProductTotalPrice(product.getPrice()*quantity );
            CartItems savedCartItem = cartItemRepository.save( cartItems );


         cart.getCartItems().add( savedCartItem );


            listCartItemsCache.invalidate( "cartItemsList" );
            return "product added successfully to the cart";
        }
        try {
            throw new CartException("item already exist in cart");
        } catch (CartException e) {
            throw new RuntimeException( e );
        }



    }

    @SneakyThrows
    @Override
    public String addToCartWithParams(String productName, int quantity, String email) {
        ProductDto product = cartServiceConfig.getProductByProductName( productName );

        if (quantity> product.getQuantity()){
            throw new CartException( "no more quantity for this product, total available quantity : "+product.getQuantity() );
        }

        if(product.getProductName().equals( "default" )){

            throw new CartException( "Product server is down ,Please try later" );
        }


        Cart cart = cartRepository.findByEmail(email  );

        CartItems cartItemsExists = cartItemRepository.isCartItemsExists( cart, email, productName );


        if (cartItemsExists==null){
            CartItems cartItems=new CartItems();
            cartItems.setCart( cart );
            cartItems.setProductName( product.getProductName() );
            cartItems.setEmail( email );
            cartItems.setPrice( product.getPrice() );
            cartItems.setQuantity( quantity);
            cartItems.setImageUrl( product.getImageUrl() );
            cartItems.setProductTotalPrice(product.getPrice()*quantity );
            CartItems savedCartItem = cartItemRepository.save( cartItems );


            cart.getCartItems().add( savedCartItem );


            listCartItemsCache.invalidate( "cartItemsList" );
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

        listCartItemsCache.invalidate( "cartItemsList" );

        return "clear cart items ,email "+email;
    }

    @SneakyThrows
    @Override
    public List<CartItems> listOfItems(String email) {
        if (email == null) {
            throw new CartException( "email not exist....." );
        }

        if (listCartItemsCache.getIfPresent( "cartItemsList" )!=null){
         List<CartItems> cartItemsListCache=   listCartItemsCache.getIfPresent( "cartItemsList" );

   String cacheEmail=cartItemsListCache.stream().map( o->o.getEmail() ).findFirst().get();

   if (cacheEmail.equals( email )){
       System.out.println("getting data from cache");
       return cartItemsListCache;
   }

        }



        List<CartItems> cartItemsList = cartItemRepository.findByEmail( email );

        if (cartItemsList.size()==0){
            throw new CartException( "there is no items in the cart,please add products" );
        }
        listCartItemsCache.put( "cartItemsList",cartItemsList );

        System.out.println("getting data from database");
        return cartItemsList;
    }

    @Override
    public String deleteCartItem(Long cartItemId) {

        CartItems cartItem = cartItemRepository.findByCartItemId( cartItemId );
        cartItemRepository.delete( cartItem );
        listCartItemsCache.invalidate( "cartItemsList" );

        return "deleted cart item  ";
    }

    @Override
    public CartPriceDetails priceDetails(String email) {

        List<CartItems> cartItemsList = listOfItems( email );

        double productTotalPrice = cartItemsList.stream().mapToDouble( c -> c.getProductTotalPrice() ).sum();

       int itemsCount=cartItemsList.size();

       CartPriceDetails cartPriceDetails=CartPriceDetails.builder()
                       .totalPrice(productTotalPrice)
                               .totalItems( itemsCount )
               .build();
        return cartPriceDetails;
    }

    @Override
    public String addProductsToCart(Long productId, String email) {

        log.info( "email {}..........................................",email );

        ProductDto product = productServiceApi.getProductByProductId( productId );

        Cart cart = cartRepository.findByEmail(email  );

        CartItems cartItemsExists = cartItemRepository.isCartItemsExists( cart, email, product.getProductName() );


        if (cartItemsExists==null){
            CartItems cartItems=new CartItems();
            cartItems.setCart( cart );
            cartItems.setProductName( product.getProductName() );
            cartItems.setEmail( email );
            cartItems.setImageUrl( product.getImageUrl() );
            cartItems.setPrice( product.getPrice() );
            cartItems.setQuantity( 1);
            cartItems.setProductTotalPrice(product.getPrice()*1 );
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


}
