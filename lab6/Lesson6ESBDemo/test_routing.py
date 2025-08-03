#!/usr/bin/env python3
"""
Test script to demonstrate the routing logic for shipping services.
This simulates the ESB routing based on order amount.
"""

import requests
import json
import time

def test_routing():
    """Test the routing logic with different order amounts."""
    
    # Test orders with different amounts
    test_orders = [
        {"orderNumber": "ORDER001", "amount": 150.0},  # Should go to Normal Shipping
        {"orderNumber": "ORDER002", "amount": 175.0},  # Should go to Normal Shipping (edge case)
        {"orderNumber": "ORDER003", "amount": 200.0},  # Should go to Next Day Shipping
        {"orderNumber": "ORDER004", "amount": 300.0},  # Should go to Next Day Shipping
        {"orderNumber": "ORDER005", "amount": 100.0},  # Should go to Normal Shipping
    ]
    
    print("=== Shipping Service Routing Test ===")
    print("Orders > $175: Next Day Shipping Service (Port 8082)")
    print("Orders ≤ $175: Normal Shipping Service (Port 8083)")
    print()
    
    for order in test_orders:
        amount = order["amount"]
        order_number = order["orderNumber"]
        
        # Determine which service should handle this order
        if amount > 175.0:
            service_name = "Next Day Shipping Service"
            port = 8082
            service_type = "next-day"
        else:
            service_name = "Normal Shipping Service"
            port = 8083
            service_type = "normal"
        
        print(f"Order: {order_number}")
        print(f"Amount: ${amount}")
        print(f"Routing to: {service_name} (Port {port})")
        
        # Try to send the order to the appropriate service
        try:
            url = f"http://localhost:{port}/orders"
            response = requests.post(url, json=order, timeout=5)
            if response.status_code == 200:
                print(f"✓ Successfully sent to {service_name}")
            else:
                print(f"✗ Failed to send to {service_name} (Status: {response.status_code})")
        except requests.exceptions.ConnectionError:
            print(f"✗ {service_name} is not running on port {port}")
        except Exception as e:
            print(f"✗ Error sending to {service_name}: {e}")
        
        print("-" * 50)

def simulate_esb_routing():
    """Simulate the ESB routing logic."""
    
    print("=== ESB Routing Simulation ===")
    print("Enterprise Service Bus Routing Logic:")
    print("1. Order comes in")
    print("2. Warehouse Service processes order")
    print("3. ESB routes to appropriate shipping service based on amount")
    print()
    
    test_orders = [
        {"orderNumber": "ORDER001", "amount": 150.0},
        {"orderNumber": "ORDER002", "amount": 200.0},
        {"orderNumber": "ORDER003", "amount": 175.0},
        {"orderNumber": "ORDER004", "amount": 250.0},
    ]
    
    for order in test_orders:
        amount = order["amount"]
        order_number = order["orderNumber"]
        
        print(f"Processing Order: {order_number} (${amount})")
        print("  → Warehouse Service: Checking stock...")
        print("  → ESB Router: Analyzing order amount...")
        
        if amount > 175.0:
            print(f"  → Routing to Next Day Shipping Service (amount: ${amount} > $175)")
            print(f"  → Next Day Shipping: Processing order {order_number} with amount: ${amount}")
        else:
            print(f"  → Routing to Normal Shipping Service (amount: ${amount} ≤ $175)")
            print(f"  → Normal Shipping: Processing order {order_number} with amount: ${amount}")
        
        print()

if __name__ == "__main__":
    print("Starting Shipping Service Routing Test...")
    print()
    
    # Test actual service connections
    test_routing()
    print()
    
    # Simulate ESB routing logic
    simulate_esb_routing()
    
    print("Test completed!") 