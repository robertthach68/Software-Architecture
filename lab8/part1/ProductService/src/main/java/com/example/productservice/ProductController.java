package com.example.productservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/product/status")
    public String getProductStatus() {
        return "ProductService is running and registered with Eureka!";
    }

    @GetMapping("/product/info")
    public String getProductInfo() {
        return "ProductService - Port: 8901, Service: Product Management";
    }
}
