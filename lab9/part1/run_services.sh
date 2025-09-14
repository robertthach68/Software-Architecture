#!/bin/bash

echo "Lab 9 Part 1: Distributed Tracing with Zipkin and Sleuth"
echo "========================================================"

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
check_port 9411 || echo "Warning: Port 9411 (Zipkin) is not available. Make sure Zipkin is not already running."

echo "All required ports are available."

# Build all services
echo "Building all services..."
cd StockService1 && mvn clean compile -q && cd ..
cd StockService2 && mvn clean compile -q && cd ..
cd ProductService && mvn clean compile -q && cd ..
cd EurekaServer && mvn clean compile -q && cd ..

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
echo "Zipkin Tracing: http://localhost:9411/zipkin"
echo ""
echo "Testing Distributed Tracing:"
echo "1. Make sure Zipkin is running (use ./download_and_start_zipkin.sh)"
echo "2. Generate some traffic by calling ProductService:"
echo "   curl http://localhost:8901/product/stock-value"
echo "   curl http://localhost:8901/product/stock-value"
echo "   curl http://localhost:8901/product/stock-value"
echo "3. Open Zipkin console: http://localhost:9411/zipkin"
echo "4. Click 'Search' to see traces between services"
echo "5. View service dependencies in the 'Dependencies' tab"
echo ""
echo "To stop all services, run: kill $EUREKA_PID $STOCK1_PID $STOCK2_PID $PRODUCT_PID"
echo ""
echo "Logs are available in:"
echo "- eureka.log"
echo "- stock1.log"
echo "- stock2.log"
echo "- product.log"
echo ""
echo "Zipkin Setup:"
echo "1. Run: ./download_and_start_zipkin.sh"
echo "2. Zipkin will be available at: http://localhost:9411/zipkin"
echo "3. Generate traffic to see traces"

