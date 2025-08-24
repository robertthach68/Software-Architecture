#!/bin/bash

echo "Testing Lab 8 Part 3: API Gateway"
echo "=================================="

# Test Eureka Server
echo "Testing Eureka Server..."
if curl -s "http://localhost:8761" > /dev/null; then
    echo "✓ Eureka Server is running"
else
    echo "✗ Eureka Server is not responding"
fi

# Test API Gateway
echo "Testing API Gateway..."
if curl -s "http://localhost:8080/actuator/health" > /dev/null; then
    echo "✓ API Gateway is running"
else
    echo "✗ API Gateway is not responding"
fi

# Test StockService via Gateway
echo "Testing StockService via Gateway..."
if curl -s "http://localhost:8080/api/stock/status" > /dev/null; then
    echo "✓ StockService via Gateway is working"
    echo "  Response: $(curl -s http://localhost:8080/api/stock/status)"
else
    echo "✗ StockService via Gateway is not working"
fi

# Test ProductService via Gateway
echo "Testing ProductService via Gateway..."
if curl -s "http://localhost:8080/api/product/status" > /dev/null; then
    echo "✓ ProductService via Gateway is working"
    echo "  Response: $(curl -s http://localhost:8080/api/product/status)"
else
    echo "✗ ProductService via Gateway is not working"
fi

# Test Load Balancing via Gateway
echo "Testing Load Balancing via Gateway..."
echo "Calling ProductService -> StockService multiple times:"
echo ""

for i in {1..5}; do
    echo "Call $i: $(curl -s http://localhost:8080/api/product/stock-value)"
    sleep 1
done

echo ""
echo "Direct Service Access via Gateway:"
echo "=================================="

# Test direct StockService access
echo "Direct StockService: $(curl -s http://localhost:8080/stock/value)"

# Test direct ProductService access
echo "Direct ProductService: $(curl -s http://localhost:8080/product/status)"

echo ""
echo "Service URLs:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- API Gateway: http://localhost:8080"
echo "- StockService1: http://localhost:8900"
echo "- StockService2: http://localhost:8902"
echo "- ProductService: http://localhost:8901"
echo ""
echo "Gateway Endpoints:"
echo "- StockService via Gateway: http://localhost:8080/api/stock/**"
echo "- ProductService via Gateway: http://localhost:8080/api/product/**"
echo "- Direct StockService: http://localhost:8080/stock/**"
echo "- Direct ProductService: http://localhost:8080/product/**"
echo ""
echo "Browser Testing:"
echo "Open test-gateway.html in your browser to test all endpoints through the gateway"
