package com.example.stockservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @GetMapping("/{productNumber}")
    public StockResponse getStockQuantity(@PathVariable String productNumber) {
        // Hard-coded stock quantities for demonstration
        int stockQuantity;
        switch (productNumber) {
            case "P001":
                stockQuantity = 150;
                break;
            case "P002":
                stockQuantity = 75;
                break;
            case "P003":
                stockQuantity = 200;
                break;
            case "P004":
                stockQuantity = 50;
                break;
            case "P005":
                stockQuantity = 300;
                break;
            default:
                stockQuantity = 0;
                break;
        }

        return new StockResponse(productNumber, stockQuantity);
    }

    public static class StockResponse {
        private String productNumber;
        private int quantity;

        public StockResponse(String productNumber, int quantity) {
            this.productNumber = productNumber;
            this.quantity = quantity;
        }

        public String getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}