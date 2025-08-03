package com.example.productservice.service;

import com.example.productservice.client.StockServiceClient;
import com.example.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private StockServiceClient stockServiceClient;

    public Product getProduct(String productNumber) {
        // Get stock quantity from StockService
        StockServiceClient.StockResponse stockResponse = stockServiceClient.getStockQuantity(productNumber);

        // Get product name based on product number
        String productName = getProductName(productNumber);

        return new Product(productNumber, productName, stockResponse.getQuantity());
    }

    private String getProductName(String productNumber) {
        // Hard-coded product names for demonstration
        switch (productNumber) {
            case "P001":
                return "Laptop Computer";
            case "P002":
                return "Wireless Mouse";
            case "P003":
                return "USB Keyboard";
            case "P004":
                return "External Hard Drive";
            case "P005":
                return "Wireless Headphones";
            default:
                return "Unknown Product";
        }
    }
}