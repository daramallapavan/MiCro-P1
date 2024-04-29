package com.example.CartService.PaymentService.serviceImpl;

import com.example.CartService.PaymentService.dto.OrderDto;
import com.example.CartService.PaymentService.dto.OrdersItems;
import com.example.CartService.PaymentService.dto.ProductUpdateDto;
import com.example.CartService.PaymentService.entity.Payment;
import com.example.CartService.PaymentService.exception.PaymentException;
import com.example.CartService.PaymentService.repository.PaymentRepository;
import com.example.CartService.PaymentService.service.PaymentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;
    @SneakyThrows
    @Override
    public String doPay(String orderNumber) {

        Optional<Payment> paymentOptional = paymentRepository.findByOrderNumber( orderNumber );
        if (paymentOptional.isPresent()){
            throw new PaymentException( "payment already done with "+orderNumber );
        }
        OrderDto  order=null;

        try{
            order = restTemplate.getForObject( "http://ORDER-SERVICE/order/getOrderByOrderNumber?orderNumber=" + orderNumber, OrderDto.class );
        }catch(HttpServerErrorException httpServerErrorException){
            throw new PaymentException( httpServerErrorException.getMessage() );
        }catch(Exception e){
            System.out.println("exception from payment service ");
        }






        Payment payment=new Payment();
        payment.setPaymentStatus( "SUCCESS" );
        payment.setPaymentType( "COD" );
        payment.setTotalOrderAmount( order.getTotalOrderPrice() );
        payment.setOrderNumber( orderNumber );
        payment.setPaymentNumber( UUID.randomUUID().toString() );
        payment.setEmail( order.getEmail() );
        Payment savedPayment= paymentRepository.save( payment );

        if (savedPayment.getPaymentStatus().equals( "SUCCESS" )){
            restTemplate.put( "http://ORDER-SERVICE/order/updateOrder?orderNumber=" + orderNumber,String.class );
        }





        ProductUpdateDto updateDto=new ProductUpdateDto();


        List<OrdersItems> ordersItemsList = order.getOrdersItemsList();

        for (OrdersItems orderItems:ordersItemsList){

            orderItems.getProductName();


            updateDto.setProductName( orderItems.getProductName() );
            updateDto.setQuantity( orderItems.getQuantity() );
            try{
                restTemplate.put( "http://PRODUCT-SERVICE/product/update", updateDto );
            }catch (HttpServerErrorException ex){
                throw new PaymentException( ex.getMessage() );
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }




        return "payment success "+savedPayment.getPaymentNumber();

    }

    @SneakyThrows
    @Override
    public Payment getPayment(String orderNumber) {
       Optional<Payment> payment= paymentRepository.findByOrderNumber(orderNumber);

       if (payment.isPresent()){
           return payment.get();
       }

       throw new PaymentException( "failed to get payment object, invalid order number" );
    }
}
