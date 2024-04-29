package com.example.CartService.OrderService.controller;

import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.ShippingAddress;
import com.example.CartService.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getUserOrders")
    public List<Orders> getUserOrders(@RequestParam String email){
        return orderService.getUserOrders(email);
    }
    @PostMapping("/createOrder")
    public String placeOrder(@RequestBody ShippingAddress shippingAddress, @RequestParam String email){
        return orderService.placeOrder(shippingAddress,email);
    }

    @GetMapping("/getOrderByOrderNumber")
    public Orders getOrderByOrderNumber(@RequestParam String orderNumber){
        return orderService.getOrderByOrderNumber(orderNumber);
    }

    @DeleteMapping("/removeOrder")
    public String deleteOrder(@RequestParam String orderNumber){
        return  orderService.deleteOrder(orderNumber);

    }

    @PutMapping("/placeOrder")
    public String changeToPlaceOrder(@RequestParam String orderNumber){
        return orderService.changeToOrderPlaced(orderNumber);


    }


    @PutMapping("/updateOrder")
    public String updateOrder(@RequestParam String orderNumber){
        return orderService.updateOrder(orderNumber);
    }


}
