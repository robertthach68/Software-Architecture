package com.example.stockservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Value("${stock.instance}")
    private int instance;

    @Value("${stock.value}")
    private int stockValue;

    @GetMapping("/stock/status")
    public String getStockStatus() {
        return "StockService Instance " + instance + " is running and registered with Eureka!";
    }

    @GetMapping("/stock/info")
    public String getStockInfo() {
        return "StockService Instance " + instance + " - Port: 8900, Service: Stock Management";
    }

    @GetMapping("/stock/value")
    public String getStockValue() {
        return "StockService Instance " + instance + " - Stock Value: " + stockValue;
    }

    @GetMapping("/stock/instance")
    public String getInstanceInfo() {
        return "StockService Instance " + instance + " - Port: 8900";
    }
}
