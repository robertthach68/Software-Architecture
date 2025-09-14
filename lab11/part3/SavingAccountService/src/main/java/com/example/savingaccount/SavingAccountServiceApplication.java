package com.example.savingaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SavingAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SavingAccountServiceApplication.class, args);
    }
}
