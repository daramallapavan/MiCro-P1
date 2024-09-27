package com.example.CartService.OrderService.serviceImpl;

import com.example.CartService.OrderService.dto.*;
import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.OrdersItems;
import com.example.CartService.OrderService.entity.ShippingAddress;
import com.example.CartService.OrderService.exception.OrderException;
import com.example.CartService.OrderService.repositoty.OrderItemRepository;
import com.example.CartService.OrderService.repositoty.OrderRepository;
import com.example.CartService.OrderService.repositoty.ShippingAddressRepository;
import com.example.CartService.OrderService.service.OrderService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private OrderItemRepository orderItemRepository;

    private RestTemplate restTemplate;

    private ShippingAddressRepository shippingAddressRepository;

    private Cache<String,List<Orders>> ordersListCache;


    @PostConstruct
    public void init(){
        ordersListCache= Caffeine.newBuilder()
                .maximumSize(100 )
                .build();
    }

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            RestTemplate restTemplate,
                            ShippingAddressRepository shippingAddressRepository

                            ){
        this.orderRepository=orderRepository;
        this.orderItemRepository=orderItemRepository;
        this.restTemplate=restTemplate;
        this.shippingAddressRepository=shippingAddressRepository;

    }
    @SneakyThrows
    @Override
    public Orders placeOrder(ShippingAddress shippingAddress,String email) {
        List<CartItems> cartItems=null;
        try{




            Cart cart = restTemplate.getForObject( "http://CART-SERVICE/cart/getCart?email="+email, Cart.class );
            cartItems= cart.getCartItems();
        }catch (HttpServerErrorException httpServerErrorException){
            throw new OrderException( httpServerErrorException.getMessage() );
        }catch (Exception e){
            log.error( "exception in order-service,Main Exception Class" );
        }

        List<OrdersItems> ordersItemsList=new ArrayList<>();

        for (CartItems cartItem:cartItems){

            OrdersItems ordersItems=new OrdersItems();

            ordersItems.setPrice( cartItem.getPrice() );
            ordersItems.setQuantity( cartItem.getQuantity() );
            ordersItems.setImageUrl(cartItem.getImageUrl() );
            ordersItems.setItemTotalPrice( cartItem.getProductTotalPrice() );
            ordersItems.setProductName( cartItem.getProductName() );


            ordersItems.setEmail( email );
            OrdersItems savedOrderItems = orderItemRepository.save( ordersItems );

            ordersItemsList.add( savedOrderItems );
        }
        double totalOrderAmount = cartItems.stream().mapToDouble( item -> item.getProductTotalPrice() ).sum();

        Orders order=new Orders();

        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd");

        LocalDateTime deliveryLocalDateTime=localDateTime.plusDays( 3 );
        String deliveryBy = deliveryLocalDateTime.format( formatter );

        order.setOrderNumber( UUID.randomUUID().toString() );
        order.setStatus( "CREATED" );
        order.setEmail( email );
        order.setOrdersItemsList( ordersItemsList );
        order.setPaymentStatus( "FAILED" );
        order.setTotalOrderPrice( totalOrderAmount );
        order.setCreatedAt( localDateTime);
        order.setDeliveryBy( deliveryBy );
        order.setTotalItems( cartItems.size() );

        Orders savedOrder = orderRepository.save( order );

        shippingAddress.setOrders( savedOrder );
        shippingAddress.setEmail( email );
        shippingAddressRepository.save( shippingAddress );


        for (OrdersItems ordersItems:ordersItemsList){
            ordersItems.setOrders( savedOrder );
            orderItemRepository.save( ordersItems );
        }

        restTemplate.delete( "http://CART-SERVICE/cart/clear?email="+email );

ordersListCache.invalidate( "ordersList" );
        return savedOrder ;
    }



    @Override
    public List<Orders> getUserOrders(String email) {

        if (ordersListCache.getIfPresent( "ordersList" )!=null){
            List<Orders> ordersListFromCache = ordersListCache.getIfPresent( "ordersList" );


            String cacheEmail = ordersListFromCache.stream().map( o -> o.getEmail() ).findFirst().get();

            System.out.println("email "+cacheEmail);
            if (cacheEmail.equals( email )){
                System.out.println("data from cache");
                return ordersListFromCache;
            }






        }

       List<Orders> ordersList=orderRepository.findByEmail(email);
        if (ordersList.size()!=0){
            ordersListCache.put( "ordersList", ordersList);
        }


        System.out.println("getting from database..........................................");
        return ordersList;
    }

    @Override
    public Orders getOrderByOrderNumber(String orderNumber) {
        Optional<Orders> order=orderRepository.findByOrderNumber(orderNumber);

        if (order.isPresent()){
            return order.get();
        }
        try {
            throw new OrderException("order not exist with this "+orderNumber);
        } catch (OrderException e) {
            throw new RuntimeException( e );
        }
    }

    @SneakyThrows
    @Override
    public String deleteOrder(String orderNumber) {
        Optional<Orders> order = orderRepository.findByOrderNumber( orderNumber );

        if (order.isEmpty()){
            throw new OrderException( "order not exist with this "+orderNumber );
        }
        orderRepository.deleteById( order.get().getId() );
        ordersListCache.invalidate( "ordersList" );
        return "order deleted successfully";
    }

    @SneakyThrows
    @Override
    public String changeToOrderPlaced(String orderNumber) {
        PaymentDto payment=null;
        try {
             payment = restTemplate.getForObject( "http://PAYMENT-SERVICE/payment/getPayment?orderNumber=" + orderNumber, PaymentDto.class );
        }catch(HttpServerErrorException httpServerErrorException){
           throw new OrderException( httpServerErrorException.getMessage() );
        }catch(Exception e){
            System.out.println("ex from order service to get payment");
        }

        if (payment.getPaymentStatus().equals( "SUCCESS" )){

            Orders order=   orderRepository.findByOrderNumber( orderNumber ).get();

            order.setStatus( "PLACED" );
            order.setPaymentStatus( "SUCCESS" );
            orderRepository.save( order );

        }
        return "order placed successfully, change created to placed";
    }

    @SneakyThrows
    @Override
    public String updateOrder(String orderNumber) {
        Optional<Orders> byOrderNumber = orderRepository.findByOrderNumber( orderNumber);

        if (byOrderNumber.isEmpty()){
            throw new OrderException( "not found" );
        }
       Orders order= byOrderNumber.get();
        order.setPaymentStatus( "SUCCESS" );
        order.setStatus( "PLACED" );
        orderRepository.save( order );

        return "updated order";
    }

    @SneakyThrows
    @Override
    public Set<ShippingAddressResponseDto> getListOfShippingAddress(String email) {

        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findByEmail( email );

        Set<ShippingAddressResponseDto> responseList= shippingAddressList.stream().map( s -> entityToDto( s ) ).collect( Collectors.toSet() );

        if (shippingAddressList.isEmpty()){
            throw new OrderException( "not exist" );
        }

        return responseList;
    }

    @Override
    public Orders createOrderWithSingleProduct(String productName, String email,ShippingAddress shippingAddress) {

        List<OrdersItems> ordersItemsList=new ArrayList<>();

        ProductDto product = restTemplate.
                getForObject( "http://PRODUCT-SERVICE/product/getProduct?productName=" + productName, ProductDto.class );



        OrdersItems ordersItem=new OrdersItems();
        ordersItem.setProductName( product.getProductName() );
        ordersItem.setPrice( product.getPrice() );
        ordersItem.setQuantity( 1 );
        ordersItem.setEmail( email );
        ordersItem.setItemTotalPrice( product.getPrice()*1);

        OrdersItems savedOrderItem = orderItemRepository.save( ordersItem );


        ordersItemsList.add( savedOrderItem );


        Orders order=new Orders();

        order.setOrderNumber( UUID.randomUUID().toString() );
        order.setStatus( "CREATED" );
        order.setEmail( email );
        order.setOrdersItemsList( ordersItemsList );
        order.setPaymentStatus( "FAILED" );

        order.setTotalOrderPrice( ordersItem.getItemTotalPrice() );
        order.setTotalItems( ordersItemsList.size() );


        Orders savedOrder = orderRepository.save( order );

        shippingAddress.setOrders( savedOrder );
        shippingAddress.setEmail( email );
        ShippingAddress savedShipping = shippingAddressRepository.save( shippingAddress );

        for (OrdersItems ordersItems:ordersItemsList){
            ordersItems.setOrders( savedOrder );
            orderItemRepository.save( ordersItems );
        }


       savedOrder.setShippingAddress(  savedShipping);
        ordersListCache.invalidate( "ordersList" );
        return savedOrder ;


    }

    private ShippingAddressResponseDto entityToDto(ShippingAddress shippingAddress) {
        ShippingAddressResponseDto response=new ShippingAddressResponseDto();
        response.setName( shippingAddress.getName() );
        response.setCity( shippingAddress.getCity() );
        response.setAddressLine1( shippingAddress.getAddressLine1() );
        response.setAddressLine2( shippingAddress.getAddressLine2() );
        response.setPhoneNumber( shippingAddress.getPhoneNumber() );
        response.setLandMark( shippingAddress.getLandMark() );
        response.setPinCode( shippingAddress.getPinCode() );
        return response;
    }


}
