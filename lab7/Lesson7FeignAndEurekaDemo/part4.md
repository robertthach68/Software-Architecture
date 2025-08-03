# Part 4: Solving Service Unavailability Problems

## Introduction

In microservice architectures, services often depend on each other. When a service becomes unavailable, it can cause cascading failures throughout the system. This document explores strategies to handle service unavailability and ensure system resilience.

## The Problem: Service Dependencies and Failures

### Understanding the Problem
When the StockService is down:
- ProductService cannot retrieve stock information
- API calls to ProductService fail or return errors
- Users experience degraded service or complete failure
- The entire system becomes unreliable

### Impact Analysis
```
StockService Down → ProductService Fails → User Experience Degraded
```

## Solutions to Address Service Unavailability

### 1. **Circuit Breaker Pattern**

The Circuit Breaker pattern prevents cascading failures by monitoring service calls and temporarily stopping them when a service is down.

#### Implementation with Resilience4j

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
</dependency>
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-feign</artifactId>
</dependency>
```

#### Circuit Breaker Configuration
```yaml
# application.yml
resilience4j:
  circuitbreaker:
    instances:
      stockService:
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 60s
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
```

#### Feign Client with Circuit Breaker
```java
@FeignClient(name = "stock-service", fallback = StockServiceFallback.class)
public interface StockServiceClient {
    
    @CircuitBreaker(name = "stockService", fallbackMethod = "getStockQuantityFallback")
    @GetMapping("/api/stock/{productNumber}")
    StockResponse getStockQuantity(@PathVariable("productNumber") String productNumber);
    
    default StockResponse getStockQuantityFallback(String productNumber, Exception e) {
        // Return default stock quantity when service is down
        return new StockResponse(productNumber, 0);
    }
}
```

#### Fallback Implementation
```java
@Component
public class StockServiceFallback implements StockServiceClient {
    
    private final Map<String, Integer> cachedStockData = new ConcurrentHashMap<>();
    
    @Override
    public StockResponse getStockQuantity(String productNumber) {
        // Return cached data or default value
        int cachedQuantity = cachedStockData.getOrDefault(productNumber, 0);
        return new StockResponse(productNumber, cachedQuantity);
    }
    
    public void updateCachedStock(String productNumber, int quantity) {
        cachedStockData.put(productNumber, quantity);
    }
}
```

### 2. **Caching Strategies**

Implementing caching reduces dependency on external services and improves performance.

#### Spring Cache Implementation
```java
@Service
@EnableCaching
public class ProductService {
    
    @Autowired
    private StockServiceClient stockServiceClient;
    
    @Cacheable(value = "stockCache", key = "#productNumber")
    public int getStockQuantity(String productNumber) {
        try {
            StockResponse response = stockServiceClient.getStockQuantity(productNumber);
            return response.getQuantity();
        } catch (Exception e) {
            // Return cached value or default
            return getCachedStockQuantity(productNumber);
        }
    }
    
    @CacheEvict(value = "stockCache", key = "#productNumber")
    public void refreshStockCache(String productNumber) {
        // Clear cache for specific product
    }
    
    private int getCachedStockQuantity(String productNumber) {
        // Return last known value or default
        return 0;
    }
}
```

#### Redis Cache Configuration
```yaml
# application.yml
spring:
  redis:
    host: localhost
    port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 300000 # 5 minutes
```

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
```

### 3. **Bulkhead Pattern**

The Bulkhead pattern isolates different parts of the system to prevent failures from spreading.

#### Thread Pool Isolation
```java
@Configuration
public class ThreadPoolConfig {
    
    @Bean("stockServiceExecutor")
    public Executor stockServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("stock-service-");
        executor.initialize();
        return executor;
    }
}
```

#### Async Service Calls
```java
@Service
public class ProductService {
    
    @Autowired
    private StockServiceClient stockServiceClient;
    
    @Async("stockServiceExecutor")
    public CompletableFuture<StockResponse> getStockQuantityAsync(String productNumber) {
        try {
            StockResponse response = stockServiceClient.getStockQuantity(productNumber);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new StockResponse(productNumber, 0));
        }
    }
}
```

### 4. **Retry Pattern with Exponential Backoff**

Implement retry logic with exponential backoff to handle temporary service unavailability.

#### Retry Configuration
```yaml
# application.yml
resilience4j:
  retry:
    instances:
      stockServiceRetry:
        maxAttempts: 3
        waitDuration: 1s
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2
```

#### Retry Implementation
```java
@FeignClient(name = "stock-service")
public interface StockServiceClient {
    
    @Retry(name = "stockServiceRetry", fallbackMethod = "getStockQuantityFallback")
    @GetMapping("/api/stock/{productNumber}")
    StockResponse getStockQuantity(@PathVariable("productNumber") String productNumber);
    
    default StockResponse getStockQuantityFallback(String productNumber, Exception e) {
        return new StockResponse(productNumber, 0);
    }
}
```

### 5. **Health Checks and Monitoring**

Implement comprehensive health checks to detect service issues early.

#### Health Indicator
```java
@Component
public class StockServiceHealthIndicator implements HealthIndicator {
    
    @Autowired
    private StockServiceClient stockServiceClient;
    
    @Override
    public Health health() {
        try {
            StockResponse response = stockServiceClient.getStockQuantity("P001");
            return Health.up()
                .withDetail("stockService", "UP")
                .withDetail("responseTime", "OK")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("stockService", "DOWN")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

#### Actuator Configuration
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

### 6. **Graceful Degradation**

Implement graceful degradation to provide partial functionality when services are down.

#### Degraded Service Response
```java
@Service
public class ProductService {
    
    @Autowired
    private StockServiceClient stockServiceClient;
    
    public Product getProduct(String productNumber) {
        String productName = getProductName(productNumber);
        int stockQuantity = getStockQuantityWithFallback(productNumber);
        
        return new Product(productNumber, productName, stockQuantity);
    }
    
    private int getStockQuantityWithFallback(String productNumber) {
        try {
            StockResponse response = stockServiceClient.getStockQuantity(productNumber);
            return response.getQuantity();
        } catch (Exception e) {
            // Return cached/default value
            return getDefaultStockQuantity(productNumber);
        }
    }
    
    private int getDefaultStockQuantity(String productNumber) {
        // Return reasonable defaults based on product type
        switch (productNumber) {
            case "P001": return 100; // Laptop - moderate default
            case "P002": return 50;  // Mouse - lower default
            case "P003": return 75;  // Keyboard - moderate default
            default: return 0;
        }
    }
}
```

### 7. **Event-Driven Architecture**

Use events to decouple services and improve resilience.

#### Event Publishing
```java
@Component
public class StockEventPublisher {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void publishStockUpdate(String productNumber, int quantity) {
        StockUpdateEvent event = new StockUpdateEvent(productNumber, quantity);
        eventPublisher.publishEvent(event);
    }
}

public class StockUpdateEvent {
    private final String productNumber;
    private final int quantity;
    
    public StockUpdateEvent(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }
    
    // Getters
}
```

#### Event Handling
```java
@Component
public class StockEventProcessor {
    
    @EventListener
    public void handleStockUpdate(StockUpdateEvent event) {
        // Update local cache
        // Notify other services
        // Log the event
    }
}
```

### 8. **Service Mesh (Istio)**

Use service mesh for advanced traffic management and resilience.

#### Istio Configuration
```yaml
# stock-service-virtual-service.yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: stock-service
spec:
  hosts:
  - stock-service
  http:
  - route:
    - destination:
        host: stock-service
        subset: v1
      weight: 90
    - destination:
        host: stock-service
        subset: v2
      weight: 10
    retries:
      attempts: 3
      perTryTimeout: 2s
    timeout: 5s
```

## Implementation Strategy for Our Services

### Step 1: Add Resilience Dependencies
```xml
<!-- Add to both services' pom.xml -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

### Step 2: Configure Circuit Breaker
```yaml
# application.yml for ProductService
resilience4j:
  circuitbreaker:
    instances:
      stockService:
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 60s
  retry:
    instances:
      stockServiceRetry:
        maxAttempts: 3
        waitDuration: 1s
```

### Step 3: Implement Fallback Logic
```java
@Service
public class ProductService {
    
    @Autowired
    private StockServiceClient stockServiceClient;
    
    @Cacheable(value = "stockCache", key = "#productNumber")
    public Product getProduct(String productNumber) {
        String productName = getProductName(productNumber);
        int stockQuantity = getStockQuantityWithResilience(productNumber);
        
        return new Product(productNumber, productName, stockQuantity);
    }
    
    private int getStockQuantityWithResilience(String productNumber) {
        try {
            StockResponse response = stockServiceClient.getStockQuantity(productNumber);
            return response.getQuantity();
        } catch (Exception e) {
            log.warn("StockService unavailable, using cached/default value for {}", productNumber);
            return getDefaultStockQuantity(productNumber);
        }
    }
}
```

### Step 4: Add Monitoring
```java
@Component
public class ServiceHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        
        // Check StockService health
        try {
            // Health check logic
            details.put("stockService", "UP");
        } catch (Exception e) {
            details.put("stockService", "DOWN");
            details.put("error", e.getMessage());
        }
        
        return Health.up().withDetails(details).build();
    }
}
```

## Best Practices

### 1. **Design for Failure**
- Assume services will fail
- Implement fallback mechanisms
- Use timeouts and retries

### 2. **Monitor and Alert**
- Set up comprehensive monitoring
- Create alerts for service failures
- Track service dependencies

### 3. **Test Failure Scenarios**
- Test circuit breaker behavior
- Validate fallback mechanisms
- Load test with service failures

### 4. **Document Dependencies**
- Maintain service dependency maps
- Document fallback strategies
- Create runbooks for failures

## Conclusion

Service unavailability is a common challenge in microservice architectures. By implementing these strategies:

1. **Circuit Breakers**: Prevent cascading failures
2. **Caching**: Reduce dependency on external services
3. **Retry Logic**: Handle temporary failures
4. **Graceful Degradation**: Provide partial functionality
5. **Monitoring**: Early detection of issues
6. **Health Checks**: Proactive service monitoring

We can build resilient systems that continue to function even when individual services are down, providing a better user experience and maintaining system reliability. 