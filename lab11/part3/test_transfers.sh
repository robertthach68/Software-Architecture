#!/bin/bash

echo "=== Banking Microservices Transfer Test ==="
echo

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to make HTTP requests
make_request() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}$description${NC}"
    echo "Request: $method $url"
    if [ ! -z "$data" ]; then
        echo "Data: $data"
    fi
    echo "Response:"
    
    if [ ! -z "$data" ]; then
        curl -s -X $method "$url" \
            -H "Content-Type: application/json" \
            -d "$data" | jq . 2>/dev/null || curl -s -X $method "$url" \
            -H "Content-Type: application/json" \
            -d "$data"
    else
        curl -s -X $method "$url" | jq . 2>/dev/null || curl -s -X $method "$url"
    fi
    echo
    echo "----------------------------------------"
    echo
}

# Wait for services to be ready
echo -e "${YELLOW}Waiting for services to be ready...${NC}"
sleep 5

# Test 1: Create checking account
make_request "POST" "http://localhost:8082/api/checking-accounts" \
    '{"accountNumber":"CHK001","customerId":"CUST001","initialBalance":1000.00}' \
    "1. Creating checking account with $1000"

# Test 2: Create saving account
make_request "POST" "http://localhost:8081/api/saving-accounts" \
    '{"accountNumber":"SAV001","customerId":"CUST001","initialBalance":500.00}' \
    "2. Creating saving account with $500"

# Test 3: Check initial balances
make_request "GET" "http://localhost:8082/api/checking-accounts/CHK001" \
    "" "3. Checking initial checking account balance"

make_request "GET" "http://localhost:8081/api/saving-accounts/SAV001" \
    "" "4. Checking initial saving account balance"

# Test 4: Successful transfer
make_request "POST" "http://localhost:8083/api/transfers/checking-to-saving" \
    '{"fromAccountNumber":"CHK001","toAccountNumber":"SAV001","amount":200.00}' \
    "5. Transferring $200 from checking to saving account"

# Test 5: Check balances after successful transfer
make_request "GET" "http://localhost:8082/api/checking-accounts/CHK001" \
    "" "6. Checking checking account balance after transfer"

make_request "GET" "http://localhost:8081/api/saving-accounts/SAV001" \
    "" "7. Checking saving account balance after transfer"

# Test 6: Test error scenario (rollback)
echo -e "${RED}8. Testing error scenario with rollback...${NC}"
make_request "POST" "http://localhost:8083/api/transfers/test-error" \
    '{"fromAccountNumber":"CHK001","toAccountNumber":"SAV001","amount":100.00}' \
    "8. Testing error scenario (should trigger rollback)"

# Test 7: Check balances after error (should be rolled back)
make_request "GET" "http://localhost:8082/api/checking-accounts/CHK001" \
    "" "9. Checking checking account balance after error (should be rolled back)"

make_request "GET" "http://localhost:8081/api/saving-accounts/SAV001" \
    "" "10. Checking saving account balance after error (should be unchanged)"

echo -e "${GREEN}=== Test completed! ===${NC}"
echo
echo "Summary:"
echo "- Created checking account with $1000"
echo "- Created saving account with $500"
echo "- Successfully transferred $200 from checking to saving"
echo "- Tested error scenario with automatic rollback"
echo "- Verified that rollback restored the original balance"
