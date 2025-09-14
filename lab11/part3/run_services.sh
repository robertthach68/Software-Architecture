#!/bin/bash

echo "=== Starting Banking Microservices ==="
echo

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to start service in background
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    echo -e "${YELLOW}Starting $service_name on port $port...${NC}"
    cd "$service_dir"
    mvn spring-boot:run > "../logs/${service_name}.log" 2>&1 &
    echo $! > "../logs/${service_name}.pid"
    cd ..
    echo -e "${GREEN}$service_name started (PID: $(cat logs/${service_name}.pid))${NC}"
    sleep 3
}

# Create logs directory
mkdir -p logs

# Check if Eureka is running
echo "Checking if Eureka Server is running..."
if curl -s http://localhost:8761 > /dev/null; then
    echo -e "${GREEN}Eureka Server is running${NC}"
else
    echo -e "${YELLOW}Eureka Server is not running. Please start it first from previous labs.${NC}"
    echo "You can start it with: cd ../../lesson8demo/EurekaServer && mvn spring-boot:run"
    echo
fi

# Start services
start_service "SavingAccountService" "SavingAccountService" "8081"
start_service "CheckingAccountService" "CheckingAccountService" "8082"
start_service "TransferClient" "TransferClient" "8083"

echo
echo -e "${GREEN}All services started!${NC}"
echo
echo "Service URLs:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- SavingAccountService: http://localhost:8081"
echo "- CheckingAccountService: http://localhost:8082"
echo "- TransferClient: http://localhost:8083"
echo
echo "To test the services, run: ./test_transfers.sh"
echo
echo "To stop all services, run: ./stop_services.sh"
