#!/bin/bash

echo "Lab 8 Part 1: Eureka Cluster Demo"
echo "=================================="

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
check_port 8762 || exit 1
check_port 8900 || exit 1
check_port 8901 || exit 1

echo "All ports are available."

# Build all services
echo "Building all services..."
cd EurekaServer1 && mvn clean compile -q && cd ..
cd EurekaServer2 && mvn clean compile -q && cd ..
cd StockService && mvn clean compile -q && cd ..
cd ProductService && mvn clean compile -q && cd ..

echo "Build completed."

# Start Eureka Server 1
echo "Starting Eureka Server 1..."
cd EurekaServer1
mvn spring-boot:run > eureka1.log 2>&1 &
EUREKA1_PID=$!
cd ..

# Wait for Eureka Server 1 to be ready
wait_for_service "http://localhost:8761" "Eureka Server 1" || exit 1

# Start Eureka Server 2
echo "Starting Eureka Server 2..."
cd EurekaServer2
mvn spring-boot:run > eureka2.log 2>&1 &
EUREKA2_PID=$!
cd ..

# Wait for Eureka Server 2 to be ready
wait_for_service "http://localhost:8762" "Eureka Server 2" || exit 1

echo "Both Eureka servers are running!"
echo "Eureka Server 1: http://localhost:8761"
echo "Eureka Server 2: http://localhost:8762"

# Start StockService
echo "Starting StockService..."
cd StockService
mvn spring-boot:run > stock.log 2>&1 &
STOCK_PID=$!
cd ..

# Wait for StockService to be ready
wait_for_service "http://localhost:8900/stock/status" "StockService" || exit 1

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
echo "Eureka Server 1: http://localhost:8761 (PID: $EUREKA1_PID)"
echo "Eureka Server 2: http://localhost:8762 (PID: $EUREKA2_PID)"
echo "StockService: http://localhost:8900 (PID: $STOCK_PID)"
echo "ProductService: http://localhost:8901 (PID: $PRODUCT_PID)"
echo ""
echo "Test the services:"
echo "curl http://localhost:8900/stock/status"
echo "curl http://localhost:8901/product/status"
echo ""
echo "To stop all services, run: kill $EUREKA1_PID $EUREKA2_PID $STOCK_PID $PRODUCT_PID"
echo ""
echo "Logs are available in:"
echo "- eureka1.log"
echo "- eureka2.log"
echo "- stock.log"
echo "- product.log"
