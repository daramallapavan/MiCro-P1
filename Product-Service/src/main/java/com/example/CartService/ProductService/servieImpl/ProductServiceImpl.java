package com.example.CartService.ProductService.servieImpl;

import com.example.CartService.ProductService.dto.ProductUpdateDto;
import com.example.CartService.ProductService.entity.Product;
import com.example.CartService.ProductService.dto.ProductDto;
import com.example.CartService.ProductService.exception.ProductException;
import com.example.CartService.ProductService.repository.ProductRepository;
import com.example.CartService.ProductService.service.ProductService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private Cache<String,List<Product>> productListCache;

    private Cache<String,Product> productCache;

    @PostConstruct
    private void init(){
        productListCache=  Caffeine.newBuilder()
                .maximumSize( 100 )
                .build();

        productCache=  Caffeine.newBuilder()
                .maximumSize( 100 )
                .build();
    }

    @Override
    public Product createProduct(ProductDto productDto) {
   //    productCache.invalidate( "product" );
        productListCache.invalidate( "productList" );

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
                .imageUrl( productDto.getImageUrl() )
                .quantity( productDto.getQuantity() )
                .build();

        Product savedProduct = productRepository.save( product );



        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts(){

        if (productListCache.getIfPresent( "productList" )!=null){
           List<Product> productList= productListCache.getIfPresent( "productList" );
            System.out.println("getting products from cache");
           return productList;
        }

        List<Product> productList= productRepository.findAll();
        if (productList.isEmpty()){
            try {
                throw new ProductException( "there is no products " );
            } catch (ProductException e) {
                throw new RuntimeException( e );
            }
        }


        if (productList.size()!=0){
            productListCache.put( "productList" ,productList);
        }

        System.out.println("getting products from database");

        return productList;

    }

    @Override
    public List<Product> getAllProducts(String field) {
        List<Product> all = productRepository.findAll( Sort.by( field ) );
        return all;
    }

    @Override
    public Product getProductByProductName(String productName) {

        if (productCache.getIfPresent( "product" )!=null){
           Product productDataFromCache= productCache.getIfPresent( "product" );

           if (productDataFromCache.getProductName().equals( productName )){
               System.out.println("getting data from product cache");
               return productDataFromCache;
           }


        }
        Optional<Product> product= productRepository.findByProductName( productName );
        if (product.isPresent()){

            productCache.put( "product",product.get() );

            System.out.println("getting product from database");
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


        productListCache.invalidate( "productList" );

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


           productCache.put( "product",savedProduct );
        System.out.println("product saved in cache");

            return "product updated "+productUpdateDto.getProductName()+" "+savedProduct.getQuantity();

    }

    @Override
    public String delete() {
        productRepository.deleteAll();
        productCache.invalidate( "product" );
        productListCache.invalidate( "productList" );
        return "deleted";
    }

    @Override
    public String deleteByProduct(String productName) {
        productRepository.deleteByProductName(productName  );
        productCache.invalidate( "product" );
        productListCache.invalidate( "productList" );
        return "successfully deleted";
    }

    @Override
    public Product getProductByProductId(Long productId)  {

       return productRepository.findById( productId )
                .orElseThrow(()->new ProductException( "Wrong ProductId" ));

    }


}
