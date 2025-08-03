#!/bin/bash

echo "Starting Eureka Server..."
cd ../lab7/Lesson7FeignAndEurekaDemo/EurekaServer
./mvnw spring-boot:run &
EUREKA_PID=$!

echo "Waiting for Eureka Server to start..."
sleep 30

echo "Starting StockService..."
cd ../../../lab6/Lesson6ESBDemo/StockService
./mvnw spring-boot:run &
STOCK_PID=$!

echo "Waiting for StockService to start..."
sleep 20

echo "Starting ProductService..."
cd ../ProductService
./mvnw spring-boot:run &
PRODUCT_PID=$!

echo "All services are starting..."
echo "Eureka Server: http://localhost:8761"
echo "StockService: http://localhost:8900"
echo "ProductService: http://localhost:8901"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for user to stop
trap "echo 'Stopping services...'; kill $EUREKA_PID $STOCK_PID $PRODUCT_PID; exit" INT
wait 