package com.example.CartService.OrderService.service;

import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.ShippingAddress;

import java.util.List;

public interface OrderService {
    String placeOrder(ShippingAddress shippingAddress,String email);

    List<Orders> getUserOrders(String email);

    Orders getOrderByOrderNumber(String orderNumber);

    String deleteOrder(String orderNumber);

    String changeToOrderPlaced(String orderNumber);


    String updateOrder(String orderNumber);
}
