package com.example.CartService.OrderService.serviceImpl;

import com.example.CartService.OrderService.dto.Cart;
import com.example.CartService.OrderService.dto.CartItems;
import com.example.CartService.OrderService.dto.PaymentDto;
import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.OrdersItems;
import com.example.CartService.OrderService.entity.ShippingAddress;
import com.example.CartService.OrderService.exception.OrderException;
import com.example.CartService.OrderService.repositoty.OrderItemRepository;
import com.example.CartService.OrderService.repositoty.OrderRepository;
import com.example.CartService.OrderService.repositoty.ShippingAddressRepository;
import com.example.CartService.OrderService.service.OrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private OrderItemRepository orderItemRepository;

    private RestTemplate restTemplate;

    private ShippingAddressRepository shippingAddressRepository;

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
    public String placeOrder(ShippingAddress shippingAddress,String email) {
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
            ordersItems.setItemTotalPrice( cartItem.getProductTotalPrice() );
            ordersItems.setProductName( cartItem.getProductName() );

            ordersItems.setEmail( email );
            OrdersItems savedOrderItems = orderItemRepository.save( ordersItems );

            ordersItemsList.add( savedOrderItems );
        }
        double totalOrderAmount = cartItems.stream().mapToDouble( item -> item.getProductTotalPrice() ).sum();

        Orders order=new Orders();

        order.setOrderNumber( UUID.randomUUID().toString() );
        order.setStatus( "CREATED" );
        order.setEmail( email );
        order.setPaymentStatus( "FAILED" );
        order.setTotalOrderPrice( totalOrderAmount );
        order.setTotalItems( cartItems.size() );

        Orders savedOrder = orderRepository.save( order );

        shippingAddress.setOrders( savedOrder );

        shippingAddressRepository.save( shippingAddress );


        for (OrdersItems ordersItems:ordersItemsList){
            ordersItems.setOrders( savedOrder );
            orderItemRepository.save( ordersItems );
        }

        restTemplate.delete( "http://CART-SERVICE/cart/clear?email="+email );


        return "order created successfully "+savedOrder.getOrderNumber() ;
    }

    @Override
    public List<Orders> getUserOrders(String email) {



        return orderRepository.findByEmail(email);
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


}
