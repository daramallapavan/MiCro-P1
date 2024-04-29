package com.example.CartService.config;

import com.example.CartService.dto.ProductDto;
import com.example.CartService.exception.CartException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CartServiceConfig {

    @Autowired
    private RestTemplate restTemplate;


    @SneakyThrows
    @CircuitBreaker( name ="productInformation",fallbackMethod ="getProductData" )
    public ProductDto getProductByProductName(String productName) {


        try{
            ProductDto productDto = restTemplate.getForObject( "http://PRODUCT-SERVICE/product/getProduct?productName=" + productName, ProductDto.class );
            return productDto;
        }catch (HttpServerErrorException httpServerErrorException){
            throw new CartException( httpServerErrorException.getMessage() );
        }


    }
    private ProductDto getProductData(String productName,Exception e){
        ProductDto productDto=new ProductDto();
        productDto.setProductName( "default" );
        productDto.setQuantity( 1 );
        productDto.setPrice( 1.00 );
        return productDto;
    }







}
