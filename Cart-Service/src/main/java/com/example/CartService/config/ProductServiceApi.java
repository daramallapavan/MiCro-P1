package com.example.CartService.config;

import com.example.CartService.dto.ProductDto;
import com.example.CartService.exception.CartException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductServiceApi {


    private RestTemplate restTemplate;
    @Value( "${getProductByIdUrl}" )
    private String getProductByIdUrl;


    @SneakyThrows
    public ProductDto getProductByProductId(Long productId){

        try{
            ProductDto productDto=  restTemplate.getForObject( getProductByIdUrl+productId, ProductDto.class );
            return productDto;
        }catch (Exception e){
            throw new CartException( "Failed to Get Product data" );
        }

    }
}
