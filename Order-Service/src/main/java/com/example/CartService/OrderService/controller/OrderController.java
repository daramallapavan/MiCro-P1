package com.example.CartService.OrderService.controller;

import com.example.CartService.OrderService.dto.DeleteOrderResponseDto;
import com.example.CartService.OrderService.dto.ShippingAddressResponseDto;
import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.ShippingAddress;
import com.example.CartService.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getUserOrders")
    public List<Orders> getUserOrders(@RequestParam String email){
        return orderService.getUserOrders(email);
    }

    @PostMapping("/createOrder")
    public Orders placeOrder(@RequestBody ShippingAddress shippingAddress, @RequestParam String email){
        return orderService.placeOrder(shippingAddress,email);
    }


    @PostMapping("/createOrderWithSingleProduct")
    public Orders createOrderWithSingleProduct(@RequestBody ShippingAddress shippingAddress,@RequestParam String email,@RequestParam String productName){

        return orderService.createOrderWithSingleProduct(productName,email,shippingAddress);
    }





    @GetMapping("/getOrderByOrderNumber")
    public Orders getOrderByOrderNumber(@RequestParam String orderNumber){
        return orderService.getOrderByOrderNumber(orderNumber);
    }

    @DeleteMapping("/removeOrder")
    public ResponseEntity<DeleteOrderResponseDto> deleteOrder(@RequestParam String orderNumber){
        String s=  orderService.deleteOrder(orderNumber);

        return new ResponseEntity<>( new DeleteOrderResponseDto( s), HttpStatus.OK );

    }

    @PutMapping("/placeOrder")
    public String changeToPlaceOrder(@RequestParam String orderNumber){
        return orderService.changeToOrderPlaced(orderNumber);


    }

    @GetMapping("/shippingAddresses")
    public Set<ShippingAddressResponseDto> shippingAddresses(@RequestParam String email){
        return orderService.getListOfShippingAddress(email);
    }


    @PutMapping("/updateOrder")
    public String updateOrder(@RequestParam String orderNumber){
        return orderService.updateOrder(orderNumber);
    }


}
