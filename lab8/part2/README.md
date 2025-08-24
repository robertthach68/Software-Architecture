# Lab 8 Part 2: Load Balancing with Ribbon

This lab demonstrates load balancing using Netflix Ribbon with multiple instances of the same service (StockService) and a client service (ProductService) that distributes requests across the instances.

## Architecture

- **EurekaServer**: Port 8761, Service Discovery Server
- **StockService1**: Port 8900, Stock Value: 100, Instance: 1
- **StockService2**: Port 8902, Stock Value: 200, Instance: 2  
- **ProductService**: Port 8901, Client service with Ribbon load balancing

## Key Features

- **Service Discovery**: All services register with Eureka
- **Load Balancing**: ProductService uses Ribbon to balance requests between StockService instances
- **High Availability**: If one StockService goes down, requests are automatically routed to the remaining instance
- **Auto-recovery**: When a stopped service restarts, it automatically rejoins the load balancer

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

### 6. Test Load Balancing

Test that ProductService is load balancing calls to StockService:

```bash
# Test multiple calls to see load balancing in action
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
```

You should see responses alternating between StockService Instance 1 (Value: 100) and StockService Instance 2 (Value: 200).

### 7. Test High Availability

**Stop one StockService** (e.g., StockService1):

```bash
# Find the PID of StockService1 and kill it
# Or use Ctrl+C in the terminal where it's running
```

**Wait up to 30 seconds** for Eureka to detect the service is down.

**Check Eureka Dashboard**: Refresh http://localhost:8761. The stopped StockService should be removed from the registry.

**Test ProductService**: Call the service multiple times:
```bash
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
```

All calls should now go to the remaining StockService instance.

### 8. Test Auto-recovery

**Restart the stopped StockService**:
```bash
cd StockService1
mvn spring-boot:run
```

Wait for the service to start and register with Eureka.

**Check Eureka Dashboard**: Refresh http://localhost:8761. The restarted StockService should appear again.

**Test Load Balancing**: Call ProductService multiple times to verify load balancing is restored:
```bash
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
curl http://localhost:8901/product/stock-value
```

## Expected Results

1. **Initial Setup**: Both StockService instances should be registered in Eureka
2. **Load Balancing**: ProductService should distribute requests between both StockService instances
3. **Service Removal**: When a StockService stops, it should be removed from Eureka within 30 seconds
4. **Failover**: ProductService should continue working with the remaining StockService instance
5. **Auto-recovery**: When a stopped service restarts, it should automatically rejoin the load balancer

## Testing Endpoints

### StockService1 (Port 8900)
- `GET /stock/status` - Service status
- `GET /stock/value` - Returns stock value 100
- `GET /stock/instance` - Instance information

### StockService2 (Port 8902)
- `GET /stock/status` - Service status
- `GET /stock/value` - Returns stock value 200
- `GET /stock/instance` - Instance information

### ProductService (Port 8901)
- `GET /product/status` - Service status
- `GET /product/stock-value` - Calls StockService with load balancing
- `GET /product/stock-instance` - Gets StockService instance info with load balancing

## Load Balancing Configuration

The load balancing is configured using:

1. **@LoadBalanced annotation** on RestTemplate bean
2. **Ribbon dependency** in pom.xml
3. **Service name resolution** through Eureka

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

## Troubleshooting

- **Services not registering**: Check that Eureka server is running and accessible
- **Load balancing not working**: Verify that both StockService instances are registered in Eureka
- **Service removal delay**: Eureka takes up to 30 seconds to detect stopped services
- **Port conflicts**: Ensure all required ports (8761, 8900, 8901, 8902) are available

## Quick Start

Use the provided scripts for automated setup:

```bash
# Start all services
./run_services.sh

# Test load balancing
./test_services.sh
```

This implementation demonstrates production-ready load balancing with automatic failover and recovery capabilities.
