#!/bin/bash

echo "Stopping Lab 10 Part 2 Services..."

# Function to stop service
stop_service() {
    local service_name=$1
    if [ -f "${service_name}.pid" ]; then
        local pid=$(cat ${service_name}.pid)
        if ps -p $pid > /dev/null; then
            echo "Stopping $service_name (PID: $pid)..."
            kill $pid
            rm ${service_name}.pid
            echo "$service_name stopped"
        else
            echo "$service_name was not running"
            rm ${service_name}.pid
        fi
    else
        echo "$service_name PID file not found"
    fi
}

# Stop all services
stop_service "ConfigServer"
stop_service "ServiceAApplication"
stop_service "ServiceBApplication"

echo "All services stopped!"

