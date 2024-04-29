package com.example.CartService.StockService.config;

import com.example.CartService.StockService.entity.User;
import com.example.CartService.StockService.exception.UserNotFoundException;
import com.example.CartService.StockService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail( email );
        if (user.isEmpty()){
            throw new UserNotFoundException( "failed to login,invalid credentials" );
        }
        return user.map( CustomUserDetails::new ).orElseThrow(()-> new UserNotFoundException( "failed to login,user credentials" ));


    }
}
