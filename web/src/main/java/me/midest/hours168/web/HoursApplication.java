package me.midest.hours168.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( "me.midest.hours168" )
public class HoursApplication {

    public static void main(String[] args) {
        SpringApplication.run( HoursApplication.class, args );
    }

}