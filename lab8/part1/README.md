# Lab 8 Part 1: Eureka Cluster

This lab demonstrates a Eureka cluster with two Eureka servers working as replicas, and two microservices (StockService and ProductService) that register with both Eureka instances.

## Architecture

- **EurekaServer1**: Port 8761, registers with EurekaServer2
- **EurekaServer2**: Port 8762, registers with EurekaServer1
- **StockService**: Port 8900, registers with both Eureka servers
- **ProductService**: Port 8901, registers with both Eureka servers

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Step-by-Step Instructions

### 1. Build all services

```bash
# Build EurekaServer1
cd EurekaServer1
mvn clean compile

# Build EurekaServer2
cd ../EurekaServer2
mvn clean compile

# Build StockService
cd ../StockService
mvn clean compile

# Build ProductService
cd ../ProductService
mvn clean compile
```

### 2. Start Eureka Server 1

```bash
cd EurekaServer1
mvn spring-boot:run
```

Wait for the service to start completely. You should see logs indicating that Eureka server is running.

### 3. Start Eureka Server 2

```bash
cd EurekaServer2
mvn spring-boot:run
```

Wait for the service to start completely. Both Eureka servers should now be running as a cluster.

### 4. Check Eureka Dashboard

Open your browser and navigate to:
- Eureka Server 1: http://localhost:8761
- Eureka Server 2: http://localhost:8762

You should see both servers listed as replicas of each other in the "Instances currently registered with Eureka" section.

### 5. Start StockService

```bash
cd StockService
mvn spring-boot:run
```

### 6. Check StockService Registration

Refresh both Eureka dashboards. You should see StockService registered in both instances.

### 7. Start ProductService

```bash
cd ProductService
mvn spring-boot:run
```

### 8. Check ProductService Registration

Refresh both Eureka dashboards. You should see both StockService and ProductService registered in both instances.

### 9. Test Service Functionality

Test that the services are working:

```bash
# Test StockService
curl http://localhost:8900/stock/status
curl http://localhost:8900/stock/info

# Test ProductService
curl http://localhost:8901/product/status
curl http://localhost:8901/product/info
```

### 10. Test High Availability

Kill one of the Eureka services (e.g., EurekaServer1) and verify that:
- The remaining Eureka server continues to work
- Both StockService and ProductService continue to function
- Services can still discover each other through the remaining Eureka instance

## Expected Results

- Both Eureka servers should appear as replicas in each other's dashboard
- StockService and ProductService should be registered in both Eureka instances
- Services should continue working even if one Eureka server is down
- The cluster provides high availability for service discovery

## Troubleshooting

- If services don't register, check that both Eureka servers are running
- Ensure ports are not already in use
- Check application logs for any error messages
- Verify that the `defaultZone` URLs in application.yml files are correct
