package com.example.CartService.StockService.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaEmailService {

  /*  @Autowired
    private JavaMailSender javaMailSender;

    public void simpleMimeMessage(String to,String subject,String text){

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo( to );
        simpleMailMessage.setSubject( subject );
        simpleMailMessage.setText( text );

        javaMailSender.send( simpleMailMessage );
    }*/
}
