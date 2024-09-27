package com.example.CartService.OrderService.service;

import com.example.CartService.OrderService.dto.ShippingAddressResponseDto;
import com.example.CartService.OrderService.entity.Orders;
import com.example.CartService.OrderService.entity.ShippingAddress;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Orders placeOrder(ShippingAddress shippingAddress,String email);



    List<Orders> getUserOrders(String email);

    Orders getOrderByOrderNumber(String orderNumber);

    String deleteOrder(String orderNumber);

    String changeToOrderPlaced(String orderNumber);


    String updateOrder(String orderNumber);

    Set<ShippingAddressResponseDto> getListOfShippingAddress(String email);

    Orders createOrderWithSingleProduct(String productName, String email,ShippingAddress shippingAddress);
}
