# Lab 8 Part 3: API Gateway

This lab demonstrates an API Gateway that sits in front of the ProductService and StockService, allowing browsers and clients to call both services through a single gateway endpoint.

## Architecture

- **EurekaServer**: Port 8761, Service Discovery Server
- **StockService1**: Port 8900, Stock Value: 100, Instance: 1
- **StockService2**: Port 8902, Stock Value: 200, Instance: 2  
- **ProductService**: Port 8901, Client service with Ribbon load balancing
- **API Gateway**: Port 8080, Single entry point for all services

## Key Features

- **Single Entry Point**: All services accessible through one gateway URL
- **Service Discovery**: Gateway automatically discovers services through Eureka
- **Load Balancing**: Gateway routes requests to appropriate service instances
- **URL Rewriting**: Clean API endpoints with automatic path mapping
- **Browser Access**: Services can be called directly from web browsers
- **Health Monitoring**: Gateway provides health and metrics endpoints

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Step-by-Step Instructions

### 1. Build all services

```bash
# Build EurekaServer
cd EurekaServer
mvn clean compile

# Build StockService1
cd ../StockService1
mvn clean compile

# Build StockService2
cd ../StockService2
mvn clean compile

# Build ProductService
cd ../ProductService
mvn clean compile

# Build ApiGateway
cd ../ApiGateway
mvn clean compile
```

### 2. Start Eureka Server

```bash
cd EurekaServer
mvn spring-boot:run
```

Wait for the service to start completely. You should see logs indicating that Eureka server is running.

**Check Eureka Dashboard**: Open http://localhost:8761 in your browser to verify the server is working.

### 3. Start StockService1

```bash
cd StockService1
mvn spring-boot:run
```

Wait for the service to start completely.

**Check Eureka Dashboard**: Refresh http://localhost:8761. You should see StockService registered with 1 instance.

### 4. Start StockService2

```bash
cd StockService2
mvn spring-boot:run
```

Wait for the service to start completely.

**Check Eureka Dashboard**: Refresh http://localhost:8761. You should see StockService registered with 2 instances.

### 5. Start ProductService

```bash
cd ProductService
mvn spring-boot:run
```

Wait for the service to start completely.

**Check Eureka Dashboard**: Refresh http://localhost:8761. You should see all three services registered.

### 6. Start API Gateway

```bash
cd ApiGateway
mvn spring-boot:run
```

Wait for the service to start completely.

**Check Gateway Health**: Open http://localhost:8080/actuator/health in your browser to verify the gateway is working.

### 7. Test API Gateway

#### Test StockService via Gateway

```bash
# Test StockService status
curl http://localhost:8080/api/stock/status

# Test StockService value
curl http://localhost:8080/api/stock/value

# Test StockService instance info
curl http://localhost:8080/api/stock/instance
```

#### Test ProductService via Gateway

```bash
# Test ProductService status
curl http://localhost:8080/api/product/status

# Test ProductService -> StockService (load balanced)
curl http://localhost:8080/api/product/stock-value

# Test ProductService -> StockService instance (load balanced)
curl http://localhost:8080/api/product/stock-instance
```

#### Test Direct Service Access

```bash
# Direct StockService access
curl http://localhost:8080/stock/value

# Direct ProductService access
curl http://localhost:8080/product/status
```

### 8. Test from Browser

Open the `test-gateway.html` file in your browser to test all endpoints through the gateway interface.

## Gateway Routing Configuration

### API Routes

The gateway is configured with the following routing rules:

```yaml
routes:
  # StockService via /api/stock/**
  - id: stock-service-route
    uri: lb://StockService
    predicates:
      - Path=/api/stock/**
    filters:
      - StripPrefix=1
  
  # ProductService via /api/product/**
  - id: product-service-route
    uri: lb://ProductService
    predicates:
      - Path=/api/product/**
    filters:
      - StripPrefix=1
  
  # Direct StockService access
  - id: stock-service-direct
    uri: lb://StockService
    predicates:
      - Path=/stock/**
  
  # Direct ProductService access
  - id: product-service-direct
    uri: lb://ProductService
    predicates:
      - Path=/product/**
```

### URL Mapping

| Gateway Endpoint | Target Service | Description |
|------------------|----------------|-------------|
| `/api/stock/**` | StockService | StockService with /api prefix stripped |
| `/api/product/**` | ProductService | ProductService with /api prefix stripped |
| `/stock/**` | StockService | Direct StockService access |
| `/product/**` | ProductService | Direct ProductService access |

## Testing Endpoints

### StockService via Gateway
- `GET /api/stock/status` - Service status
- `GET /api/stock/value` - Returns stock value (load balanced)
- `GET /api/stock/instance` - Instance information (load balanced)

### ProductService via Gateway
- `GET /api/product/status` - Service status
- `GET /api/product/stock-value` - Calls StockService with load balancing
- `GET /api/product/stock-instance` - Gets StockService instance info with load balancing

### Direct Access via Gateway
- `GET /stock/value` - Direct StockService access
- `GET /product/status` - Direct ProductService access

## Expected Results

1. **Gateway Access**: All services should be accessible through the gateway at port 8080
2. **Load Balancing**: StockService calls should be distributed between both instances
3. **Service Discovery**: Gateway should automatically discover services through Eureka
4. **Browser Access**: Services should be callable from web browsers through the gateway
5. **URL Rewriting**: Clean API endpoints with automatic path mapping

## Browser Testing

The `test-gateway.html` file provides a user-friendly interface to test all gateway endpoints:

1. **Open the HTML file** in your browser
2. **Click test buttons** for each endpoint
3. **View responses** in real-time
4. **Test load balancing** with multiple calls
5. **Verify gateway functionality** across all services

## Troubleshooting

- **Gateway not starting**: Check that all services are registered in Eureka
- **Routes not working**: Verify service names match Eureka registration
- **Load balancing not working**: Ensure multiple StockService instances are running
- **Browser access issues**: Check CORS configuration if needed
- **Port conflicts**: Ensure port 8080 is available for the gateway

## Quick Start

Use the provided scripts for automated setup:

```bash
# Start all services
./run_services.sh

# Test gateway functionality
./test_gateway.sh

# Browser testing
open test-gateway.html
```

## Benefits of API Gateway

1. **Single Entry Point**: Clients only need to know one URL
2. **Service Discovery**: Automatic service location through Eureka
3. **Load Balancing**: Built-in load balancing for multiple service instances
4. **URL Management**: Clean, consistent API endpoints
5. **Monitoring**: Centralized health checks and metrics
6. **Security**: Centralized authentication and authorization (can be added)
7. **Rate Limiting**: Built-in request throttling (can be configured)

This implementation demonstrates a production-ready API Gateway that provides a unified interface for accessing multiple microservices while maintaining load balancing and service discovery capabilities.
