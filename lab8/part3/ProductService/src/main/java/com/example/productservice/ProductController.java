package com.example.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProductController {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/product/status")
    public String getProductStatus() {
        return "ProductService is running and registered with Eureka!";
    }

    @GetMapping("/product/info")
    public String getProductInfo() {
        return "ProductService - Port: 8901, Service: Product Management";
    }

    @GetMapping("/product/stock-value")
    public String getStockValue() {
        try {
            // This will use Ribbon to load balance between StockService instances
            String stockValue = restTemplate.getForObject("http://StockService/stock/value", String.class);
            return "ProductService called StockService: " + stockValue;
        } catch (Exception e) {
            return "Error calling StockService: " + e.getMessage();
        }
    }

    @GetMapping("/product/stock-instance")
    public String getStockInstance() {
        try {
            // This will use Ribbon to load balance between StockService instances
            String instanceInfo = restTemplate.getForObject("http://StockService/stock/instance", String.class);
            return "ProductService called StockService: " + instanceInfo;
        } catch (Exception e) {
            return "Error calling StockService: " + e.getMessage();
        }
    }
}
