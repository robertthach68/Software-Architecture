package com.example.checkingaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CheckingAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckingAccountServiceApplication.class, args);
    }
}
