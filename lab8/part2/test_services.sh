#!/bin/bash

echo "Testing Lab 8 Part 2: Load Balancing with Ribbon"
echo "================================================"

# Test Eureka Server
echo "Testing Eureka Server..."
if curl -s "http://localhost:8761" > /dev/null; then
    echo "✓ Eureka Server is running"
else
    echo "✗ Eureka Server is not responding"
fi

# Test StockService1
echo "Testing StockService1..."
if curl -s "http://localhost:8900/stock/status" > /dev/null; then
    echo "✓ StockService1 is running"
    echo "  Response: $(curl -s http://localhost:8900/stock/status)"
    echo "  Stock Value: $(curl -s http://localhost:8900/stock/value)"
else
    echo "✗ StockService1 is not responding"
fi

# Test StockService2
echo "Testing StockService2..."
if curl -s "http://localhost:8902/stock/status" > /dev/null; then
    echo "✓ StockService2 is running"
    echo "  Response: $(curl -s http://localhost:8902/stock/status)"
    echo "  Stock Value: $(curl -s http://localhost:8902/stock/value)"
else
    echo "✗ StockService2 is not responding"
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
echo "Load Balancing Test:"
echo "===================="
echo "Calling ProductService multiple times to demonstrate load balancing:"
echo ""

for i in {1..5}; do
    echo "Call $i: $(curl -s http://localhost:8901/product/stock-value)"
    sleep 1
done

echo ""
echo "Service URLs:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- StockService1: http://localhost:8900"
echo "- StockService2: http://localhost:8902"
echo "- ProductService: http://localhost:8901"
echo ""
echo "Load Balancing Endpoints:"
echo "- ProductService -> StockService: http://localhost:8901/product/stock-value"
echo "- ProductService -> StockService Instance: http://localhost:8901/product/stock-instance"
