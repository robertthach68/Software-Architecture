#!/bin/bash

echo "Testing Lab 8 Part 1 Services"
echo "=============================="

# Test Eureka Server 1
echo "Testing Eureka Server 1..."
if curl -s "http://localhost:8761" > /dev/null; then
    echo "✓ Eureka Server 1 is running"
else
    echo "✗ Eureka Server 1 is not responding"
fi

# Test Eureka Server 2
echo "Testing Eureka Server 2..."
if curl -s "http://localhost:8762" > /dev/null; then
    echo "✓ Eureka Server 2 is running"
else
    echo "✗ Eureka Server 2 is not responding"
fi

# Test StockService
echo "Testing StockService..."
if curl -s "http://localhost:8900/stock/status" > /dev/null; then
    echo "✓ StockService is running"
    echo "  Response: $(curl -s http://localhost:8900/stock/status)"
else
    echo "✗ StockService is not responding"
fi

# Test ProductService
echo "Testing ProductService..."
if curl -s "http://localhost:8901/product/status" > /dev/null; then
    echo "✓ ProductService is running"
    echo "  Response: $(curl -s http://localhost:8901/product/status)"
else
    echo "✗ ProductService is not responding"
fi

echo ""
echo "Eureka Dashboard URLs:"
echo "- Eureka Server 1: http://localhost:8761"
echo "- Eureka Server 2: http://localhost:8762"
echo ""
echo "Service URLs:"
echo "- StockService: http://localhost:8900"
echo "- ProductService: http://localhost:8901"
