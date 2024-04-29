package com.example.CartService.StockService.serviceImpl;

import com.example.CartService.StockService.config.CustomUserDetailsServiceImpl;
import com.example.CartService.StockService.dto.UserRequest;
import com.example.CartService.StockService.entity.User;
import com.example.CartService.StockService.exception.UserNotFoundException;
import com.example.CartService.StockService.repository.UserRepository;
import com.example.CartService.StockService.service.UserService;
import com.example.CartService.StockService.util.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public String createUser(User user) {
        user.setPassword(passwordEncoder.encode( user.getPassword() )  );
        userRepository.save( user );


        return "user registered successfully";
    }
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Override
    public String loginAndGetToken(UserRequest userRequest) {

        String token=null;
        try {
            Authentication authenticate = authenticationManager.authenticate
                    ( new UsernamePasswordAuthenticationToken( userRequest.getEmail(), userRequest.getPassword() ) );

            if (authenticate.isAuthenticated()) {
                 token = jwtUtil.GenerateToken( userRequest.getEmail() );

            }
        }catch (Exception e) {
            throw new UserNotFoundException( "Failed to login,Incorrect Password" );


        }



        return token;

    }


    @Override
    public void validToken(String token) {
        String email = jwtUtil.extractUsername( token );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername( email );

           jwtUtil.validateToken( token, userDetails );



    }



}
