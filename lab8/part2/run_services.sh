#!/bin/bash

echo "Lab 8 Part 2: Load Balancing with Ribbon Demo"
echo "=============================================="

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo "Port $1 is already in use. Please stop the service using this port."
        return 1
    fi
    return 0
}

# Function to wait for service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    echo "Waiting for $service_name to be ready..."
    
    for i in {1..30}; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo "$service_name is ready!"
            return 0
        fi
        echo "Waiting... ($i/30)"
        sleep 2
    done
    
    echo "Timeout waiting for $service_name"
    return 1
}

# Check if required ports are available
echo "Checking ports..."
check_port 8761 || exit 1
check_port 8900 || exit 1
check_port 8901 || exit 1
check_port 8902 || exit 1

echo "All ports are available."

# Build all services
echo "Building all services..."
cd EurekaServer && mvn clean compile -q && cd ..
cd StockService1 && mvn clean compile -q && cd ..
cd StockService2 && mvn clean compile -q && cd ..
cd ProductService && mvn clean compile -q && cd ..

echo "Build completed."

# Start Eureka Server
echo "Starting Eureka Server..."
cd EurekaServer
mvn spring-boot:run > eureka.log 2>&1 &
EUREKA_PID=$!
cd ..

# Wait for Eureka Server to be ready
wait_for_service "http://localhost:8761" "Eureka Server" || exit 1

echo "Eureka Server is running!"
echo "Eureka Dashboard: http://localhost:8761"

# Start StockService1
echo "Starting StockService1..."
cd StockService1
mvn spring-boot:run > stock1.log 2>&1 &
STOCK1_PID=$!
cd ..

# Wait for StockService1 to be ready
wait_for_service "http://localhost:8900/stock/status" "StockService1" || exit 1

# Start StockService2
echo "Starting StockService2..."
cd StockService2
mvn spring-boot:run > stock2.log 2>&1 &
STOCK2_PID=$!
cd ..

# Wait for StockService2 to be ready
wait_for_service "http://localhost:8902/stock/status" "StockService2" || exit 1

# Start ProductService
echo "Starting ProductService..."
cd ProductService
mvn spring-boot:run > product.log 2>&1 &
PRODUCT_PID=$!
cd ..

# Wait for ProductService to be ready
wait_for_service "http://localhost:8901/product/status" "ProductService" || exit 1

echo ""
echo "All services are running!"
echo "========================="
echo "Eureka Server: http://localhost:8761 (PID: $EUREKA_PID)"
echo "StockService1: http://localhost:8900 (PID: $STOCK1_PID)"
echo "StockService2: http://localhost:8902 (PID: $STOCK2_PID)"
echo "ProductService: http://localhost:8901 (PID: $PRODUCT_PID)"
echo ""
echo "Test the services:"
echo "curl http://localhost:8900/stock/value    # StockService1 (Value: 100)"
echo "curl http://localhost:8902/stock/value    # StockService2 (Value: 200)"
echo "curl http://localhost:8901/product/stock-value    # ProductService with load balancing"
echo ""
echo "To stop all services, run: kill $EUREKA_PID $STOCK1_PID $STOCK2_PID $PRODUCT_PID"
echo ""
echo "Logs are available in:"
echo "- eureka.log"
echo "- stock1.log"
echo "- stock2.log"
echo "- product.log"
echo ""
echo "Load Balancing Test:"
echo "1. Call ProductService multiple times to see load balancing between StockServices"
echo "2. Stop one StockService and see how ProductService adapts"
echo "3. Restart the stopped StockService to see it rejoin the load balancer"
