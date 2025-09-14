#!/bin/bash

echo "=== Stopping Banking Microservices ==="
echo

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to stop service
stop_service() {
    local service_name=$1
    local pid_file="logs/${service_name}.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            echo -e "${YELLOW}Stopping $service_name (PID: $pid)...${NC}"
            kill $pid
            sleep 2
            if ps -p $pid > /dev/null 2>&1; then
                echo -e "${RED}Force killing $service_name...${NC}"
                kill -9 $pid
            fi
            echo -e "${GREEN}$service_name stopped${NC}"
        else
            echo -e "${YELLOW}$service_name was not running${NC}"
        fi
        rm -f "$pid_file"
    else
        echo -e "${YELLOW}No PID file found for $service_name${NC}"
    fi
}

# Stop services
stop_service "SavingAccountService"
stop_service "CheckingAccountService"
stop_service "TransferClient"

echo
echo -e "${GREEN}All services stopped!${NC}"
