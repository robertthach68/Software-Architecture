#!/bin/bash

echo "Testing Lab 10 Part 2 Services..."
echo "=================================="

# Function to test endpoint
test_endpoint() {
    local url=$1
    local description=$2
    echo ""
    echo "Testing: $description"
    echo "URL: $url"
    echo "Response:"
    curl -s "$url" | jq . 2>/dev/null || curl -s "$url"
    echo ""
}

# Wait for services to start
echo "Waiting for services to start..."
sleep 15

# Test ConfigServer endpoints
test_endpoint "http://localhost:8888/ServiceA/default" "ConfigServer - ServiceA configuration"
test_endpoint "http://localhost:8888/ServiceB/default" "ConfigServer - ServiceB configuration"

# Test Service endpoints
test_endpoint "http://localhost:8081/" "ServiceA - Main endpoint"
test_endpoint "http://localhost:8082/" "ServiceB - Main endpoint"

echo "=================================="
echo "Testing completed!"
