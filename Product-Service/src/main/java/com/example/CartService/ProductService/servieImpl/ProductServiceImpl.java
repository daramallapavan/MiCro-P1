package com.example.CartService.ProductService.servieImpl;

import com.example.CartService.ProductService.dto.ProductUpdateDto;
import com.example.CartService.ProductService.entity.Product;
import com.example.CartService.ProductService.dto.ProductDto;
import com.example.CartService.ProductService.exception.ProductException;
import com.example.CartService.ProductService.repository.ProductRepository;
import com.example.CartService.ProductService.service.ProductService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public String createProduct(ProductDto productDto) {

        Optional<Product> existedProduct=productRepository.findByProductName(productDto.getProductName());
        if (existedProduct.isPresent()){
            try {
                throw new ProductException("Product already exist with this "+productDto.getProductName());
            } catch (ProductException e) {
                throw new RuntimeException( e );
            }
        }

        Product product=Product.builder()
                .productName(productDto.getProductName() )
                .price( productDto.getPrice() )
                .quantity( productDto.getQuantity() )
                .build();

        productRepository.save( product );

        return "product created successfully with "+productDto.getProductName();
    }

    @Override
    public List<Product> getAllProducts(){
        List<Product> productList= productRepository.findAll();
        if (productList.isEmpty()){
            try {
                throw new ProductException( "there is no products " );
            } catch (ProductException e) {
                throw new RuntimeException( e );
            }
        }
        return productList;
    }

    @Override
    public Product getProductByProductName(String productName) {
        Optional<Product> product= productRepository.findByProductName( productName );
        if (product.isPresent()){
           return product.get();
        }
        try {
            throw new ProductException( "product not exist with this "+productName );
        } catch (ProductException e) {
            throw new RuntimeException( e );
        }
    }

    @SneakyThrows
    @Override
    public String updateProduct(ProductUpdateDto productUpdateDto) {
        Optional<Product> productOptional = productRepository.findByProductName( productUpdateDto.getProductName() );

        if (productOptional.isEmpty()){
            throw new ProductException( "product not exist" );

        }



            Product product=productOptional.get();

        if (product.getQuantity()< productUpdateDto.getQuantity()){

            throw new ProductException( "there is no quantity for this product  we have remaining "+product.getQuantity());
        }
            product.setQuantity( product.getQuantity()- productUpdateDto.getQuantity() );
           Product savedProduct= productRepository.save( product );

            return "product updated "+productUpdateDto.getProductName()+" "+savedProduct.getQuantity();

    }

    @Override
    public String delete() {
        productRepository.deleteAll();
        return "deleted";
    }

    @Override
    public String deleteByProduct(String productName) {
        productRepository.deleteByProductName(productName  );
        return "successfully deleted";
    }


}
