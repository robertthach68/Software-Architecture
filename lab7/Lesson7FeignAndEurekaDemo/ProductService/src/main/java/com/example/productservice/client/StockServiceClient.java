package com.example.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service")
public interface StockServiceClient {

    @GetMapping("/api/stock/{productNumber}")
    StockResponse getStockQuantity(@PathVariable("productNumber") String productNumber);

    class StockResponse {
        private String productNumber;
        private int quantity;

        public StockResponse() {
        }

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