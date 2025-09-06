#!/bin/bash

echo "Starting Lab 10 Part 2 Services..."

# Function to run service in background
run_service() {
    local service_name=$1
    local port=$2
    echo "Starting $service_name on port $port..."
    cd $service_name
    ./mvnw spring-boot:run > ../${service_name}.log 2>&1 &
    echo $! > ../${service_name}.pid
    cd ..
    echo "$service_name started with PID $(cat ${service_name}.pid)"
}

# Start ConfigServer
run_service "ConfigServer" "8888"

# Wait a bit for ConfigServer to start
echo "Waiting for ConfigServer to start..."
sleep 10

# Start ServiceAApplication
run_service "ServiceAApplication" "8081"

# Start ServiceBApplication
run_service "ServiceBApplication" "8082"

echo ""
echo "All services started!"
echo "ConfigServer: http://localhost:8888"
echo "ServiceA: http://localhost:8081"
echo "ServiceB: http://localhost:8082"
echo ""
echo "Test ConfigServer endpoints:"
echo "  http://localhost:8888/ServiceA/default"
echo "  http://localhost:8888/ServiceB/default"
echo ""
echo "To stop services, run: ./stop_services.sh"
