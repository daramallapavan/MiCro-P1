package com.example.ApiGatewayService.config;

import com.example.ApiGatewayService.exception.CustomGatewayException;
import com.example.ApiGatewayService.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;


    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            ServerHttpRequest serverRequest=null;
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new CustomGatewayException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {

                    jwtUtil.validateToken(authHeader);
                    String email = jwtUtil.extractUsername( authHeader );
                     serverRequest = exchange.getRequest().mutate().header( "email", email ).build();

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new CustomGatewayException("un authorized access to application,invalid access, token not valid");
                }
            }
            return chain.filter(exchange.mutate().request( serverRequest ).build());
        });
    }

    public static class Config {

    }
}
