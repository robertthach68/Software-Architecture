# Stock Service and Product Service with Feign Integration

This project contains two Spring Boot microservices that demonstrate service-to-service communication using Feign.

## Services Overview

### 1. StockService (Port 8900)
- **Purpose**: Returns stock quantity for a given product number
- **Port**: 8900
- **Endpoint**: `GET /api/stock/{productNumber}`
- **Response**: JSON with productNumber and quantity

### 2. ProductService (Port 8901)
- **Purpose**: Returns complete product information including stock quantity
- **Port**: 8901
- **Endpoint**: `GET /api/products/{productNumber}`
- **Features**: Uses Feign to call StockService for stock data
- **Response**: JSON with productNumber, name, and stockQuantity

## Available Products

The services support the following product numbers with hard-coded data:

| Product Number | Product Name | Stock Quantity |
|----------------|--------------|----------------|
| P001 | Laptop Computer | 150 |
| P002 | Wireless Mouse | 75 |
| P003 | USB Keyboard | 200 |
| P004 | External Hard Drive | 50 |
| P005 | Wireless Headphones | 300 |

## Running the Services with Eureka

### Prerequisites
- Java 11 or higher
- Maven

### Step 1: Start Eureka Server
```bash
cd lab7/Lesson7FeignAndEurekaDemo/EurekaServer
./mvnw spring-boot:run
```

The Eureka Server will start on port 8761. You can access the dashboard at: http://localhost:8761

### Step 2: Start StockService
```bash
cd lab6/Lesson6ESBDemo/StockService
./mvnw spring-boot:run
```

The StockService will start on port 8900 and register with Eureka.

### Step 3: Start ProductService
```bash
cd lab6/Lesson6ESBDemo/ProductService
./mvnw spring-boot:run
```

The ProductService will start on port 8901 and register with Eureka.

### Alternative: Use the provided script
```bash
cd lab6/Lesson6ESBDemo
./run_eureka_services.sh
```

This script will start all services in the correct order.

## Testing the Services

### Verify Eureka Registration
1. Open your browser and go to: http://localhost:8761
2. You should see both services registered:
   - **STOCKSERVICE** (UP)
   - **PRODUCTSERVICE** (UP)

### Test StockService directly:
```bash
curl http://localhost:8900/api/stock/P001
```
Expected response:
```json
{
  "productNumber": "P001",
  "quantity": 150
}
```

### Test ProductService (which calls StockService via Feign):
```bash
curl http://localhost:8901/api/products/P001
```
Expected response:
```json
{
  "productNumber": "P001",
  "name": "Laptop Computer",
  "stockQuantity": 150
}
```

**Note**: The ProductService now uses Eureka service discovery to find and call the StockService, rather than using a hardcoded URL.

## Architecture

- **StockService**: Simple REST service that returns stock quantities
- **ProductService**: Composite service that:
  - Provides product names based on product numbers
  - Uses Feign client to call StockService for stock data
  - Combines both pieces of information into a complete product response

## Feign Integration with Eureka

The ProductService uses Spring Cloud OpenFeign to make HTTP calls to the StockService. The Feign client is configured in `StockServiceClient.java` and automatically handles:

- HTTP request/response serialization
- Error handling
- Service discovery via Eureka registry
- Load balancing (if multiple instances are running)

The `@FeignClient(name = "stock-service")` annotation tells Feign to look up the service in the Eureka registry using the service name "stock-service".

## Dependencies

### StockService
- Spring Boot Web Starter
- Spring Cloud Netflix Eureka Client

### ProductService
- Spring Boot Web Starter
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- Spring Cloud Dependencies (for Feign and Eureka support) 