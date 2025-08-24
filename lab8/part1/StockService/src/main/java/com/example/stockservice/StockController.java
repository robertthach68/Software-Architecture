package com.example.stockservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock/status")
    public String getStockStatus() {
        return "StockService is running and registered with Eureka!";
    }

    @GetMapping("/stock/info")
    public String getStockInfo() {
        return "StockService - Port: 8900, Service: Stock Management";
    }
}
