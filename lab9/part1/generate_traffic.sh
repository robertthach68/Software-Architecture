#!/bin/bash

echo "Lab 9 Part 1: Generating Traffic for Distributed Tracing"
echo "========================================================"

echo "This script will generate traffic between services to demonstrate distributed tracing."
echo "Make sure all services are running and Zipkin is available at http://localhost:9411/zipkin"
echo ""

# Check if services are running
echo "Checking if services are running..."

if ! curl -s "http://localhost:8761" > /dev/null; then
    echo "✗ Eureka Server is not running at http://localhost:8761"
    echo "Please start the services first using ./run_services.sh"
    exit 1
fi

if ! curl -s "http://localhost:8901/product/status" > /dev/null; then
    echo "✗ ProductService is not running at http://localhost:8901"
    echo "Please start the services first using ./run_services.sh"
    exit 1
fi

echo "✓ Services are running"
echo ""

# Generate traffic
echo "Generating traffic for distributed tracing..."
echo "This will create traces that you can view in Zipkin at http://localhost:9411/zipkin"
echo ""

echo "Call 1: ProductService -> StockService (Load Balanced)"
curl -s "http://localhost:8901/product/stock-value"
echo ""

echo "Call 2: ProductService -> StockService (Load Balanced)"
curl -s "http://localhost:8901/product/stock-value"
echo ""

echo "Call 3: ProductService -> StockService (Load Balanced)"
curl -s "http://localhost:8901/product/stock-value"
echo ""

echo "Call 4: ProductService -> StockService (Load Balanced)"
curl -s "http://localhost:8901/product/stock-value"
echo ""

echo "Call 5: ProductService -> StockService (Load Balanced)"
curl -s "http://localhost:8901/product/stock-value"
echo ""

echo "Call 6: Direct StockService access"
curl -s "http://localhost:8900/stock/value"
echo ""

echo "Call 7: Direct StockService access"
curl -s "http://localhost:8902/stock/value"
echo ""

echo "Call 8: ProductService status"
curl -s "http://localhost:8901/product/status"
echo ""

echo "Call 9: StockService status (Instance 1)"
curl -s "http://localhost:8900/stock/status"
echo ""

echo "Call 10: StockService status (Instance 2)"
curl -s "http://localhost:8902/stock/status"
echo ""

echo ""
echo "Traffic generation completed!"
echo ""
echo "Now you can view the traces in Zipkin:"
echo "1. Open http://localhost:9411/zipkin in your browser"
echo "2. Click the 'Search' button"
echo "3. You should see traces for the calls we just made"
echo "4. Click on any trace to see the detailed flow between services"
echo "5. Go to the 'Dependencies' tab to see service relationships"
echo ""
echo "Each trace will show:"
echo "- The complete request flow through all services"
echo "- Timing information for each service call"
echo "- Service dependencies and relationships"
echo "- Load balancing between StockService instances"
echo ""
echo "This demonstrates how Sleuth and Zipkin provide visibility into:"
echo "- Service-to-service communication"
echo "- Request flow across the microservice architecture"
echo "- Performance metrics and timing"
echo "- Service dependencies and topology"
