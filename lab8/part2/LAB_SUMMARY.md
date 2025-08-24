# Lab 8 Part 2: Load Balancing with Ribbon - Implementation Summary

## What Has Been Implemented

### 1. Service Architecture
- **EurekaServer** (Port 8761): Single Eureka server for service discovery
- **StockService1** (Port 8900): First instance returning stock value 100
- **StockService2** (Port 8902): Second instance returning stock value 200
- **ProductService** (Port 8901): Client service with Ribbon load balancing

### 2. Key Configuration Files

#### EurekaServer/application.yml
```yaml
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 1000
```

#### StockService1/application.yml
```yaml
server:
  port: 8900
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: StockService
stock:
  instance: 1
  value: 100
```

#### StockService2/application.yml
```yaml
server:
  port: 8902
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: StockService
stock:
  instance: 2
  value: 200
```

#### ProductService/application.yml
```yaml
server:
  port: 8901
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: ProductService
ribbon:
  eureka:
    enabled: true
  ReadTimeout: 5000
  ConnectTimeout: 5000
```

### 3. Load Balancing Implementation

#### ProductService Controller
```java
@RestController
public class ProductController {
    
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @GetMapping("/product/stock-value")
    public String getStockValue() {
        // This will use Ribbon to load balance between StockService instances
        String stockValue = restTemplate.getForObject("http://StockService/stock/value", String.class);
        return "ProductService called StockService: " + stockValue;
    }
}
```

#### Key Load Balancing Features
1. **@LoadBalanced annotation** on RestTemplate bean
2. **Service name resolution** through Eureka (`http://StockService/...`)
3. **Automatic instance selection** by Ribbon
4. **Failover handling** when instances go down

### 4. Project Structure
```
lab8/part2/
├── EurekaServer/
│   ├── src/main/java/com/example/eurekaserver/
│   │   └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── StockService1/
│   ├── src/main/java/com/example/stockservice/
│   │   ├── StockService1Application.java
│   │   └── StockController.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── StockService2/
│   ├── src/main/java/com/example/stockservice/
│   │   ├── StockService2Application.java
│   │   └── StockController.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── ProductService/
│   ├── src/main/java/com/example/productservice/
│   │   ├── ProductServiceApplication.java
│   │   └── ProductController.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── run_services.sh
├── test_services.sh
├── README.md
└── LAB_SUMMARY.md
```

### 5. Dependencies
All services use:
- Spring Boot 2.7.0
- Spring Cloud 2021.0.3
- Netflix Ribbon for load balancing
- Java 11

### 6. Key Features Implemented

#### Load Balancing
- ProductService automatically distributes requests between StockService instances
- Round-robin load balancing by default
- Service name resolution through Eureka

#### High Availability
- If one StockService goes down, requests automatically go to the remaining instance
- No manual configuration required for failover

#### Auto-recovery
- When a stopped service restarts, it automatically rejoins the load balancer
- Eureka automatically detects service health and updates the registry

#### Service Discovery
- All services register with Eureka
- ProductService discovers StockService instances through Eureka
- Dynamic service registration and deregistration

### 7. Testing Endpoints

#### StockService1 (Port 8900)
- `GET /stock/status` - Returns service status with instance info
- `GET /stock/value` - Returns stock value 100
- `GET /stock/instance` - Returns instance information

#### StockService2 (Port 8902)
- `GET /stock/status` - Returns service status with instance info
- `GET /stock/value` - Returns stock value 200
- `GET /stock/instance` - Returns instance information

#### ProductService (Port 8901)
- `GET /product/status` - Returns service status
- `GET /product/stock-value` - Calls StockService with load balancing
- `GET /product/stock-instance` - Gets StockService instance info with load balancing

### 8. Load Balancing Behavior

#### Normal Operation
- Requests are distributed between both StockService instances
- You can see alternating responses (Instance 1: Value 100, Instance 2: Value 200)

#### Service Failure
- When one StockService stops, Eureka removes it from registry (within 30 seconds)
- ProductService automatically routes all requests to the remaining instance
- No service interruption for clients

#### Service Recovery
- When a stopped service restarts, it re-registers with Eureka
- ProductService automatically starts load balancing again
- Seamless recovery without manual intervention

### 9. How to Run

1. **Quick Start**: Run `./run_services.sh` to start all services automatically
2. **Manual Start**: Follow the step-by-step instructions in README.md
3. **Testing**: Use `./test_services.sh` to verify load balancing functionality

### 10. Expected Results

1. **Initial Setup**: Both StockService instances register with Eureka
2. **Load Balancing**: ProductService distributes requests between both instances
3. **Service Removal**: Stopped services are removed from Eureka within 30 seconds
4. **Failover**: ProductService continues working with remaining instances
5. **Auto-recovery**: Restarted services automatically rejoin the load balancer

### 11. Verification Steps

1. Check Eureka dashboard shows both StockService instances
2. Test ProductService multiple times to see load balancing
3. Stop one StockService and verify failover
4. Restart the stopped service and verify load balancing is restored
5. Monitor Eureka dashboard for service registration changes

This implementation demonstrates production-ready load balancing with Netflix Ribbon, providing high availability, automatic failover, and seamless service recovery in a microservice architecture.
