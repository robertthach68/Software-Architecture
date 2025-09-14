# Lab 11 Part 3 - Banking Microservices with Distributed Transactions

This project implements two banking microservices (SavingAccountService and CheckingAccountService) with a client application that handles money transfers between accounts using distributed transaction patterns.

## Architecture

- **SavingAccountService** (Port 8081): Manages saving accounts
- **CheckingAccountService** (Port 8082): Manages checking accounts  
- **TransferClient** (Port 8083): Handles transfers between accounts
- **EurekaServer** (Port 8761): Service discovery (needs to be running)

## Services

### SavingAccountService
- Create saving accounts
- Deposit/withdraw money
- Transfer operations (from/to)

### CheckingAccountService
- Create checking accounts
- Deposit/withdraw money
- Transfer operations (from/to)

### TransferClient
- Transfer money between checking and saving accounts
- Implements distributed transaction pattern with rollback capability
- Error simulation for testing transaction rollback

## Running the Services

1. Start Eureka Server (from previous labs)
2. Start SavingAccountService
3. Start CheckingAccountService
4. Start TransferClient

## API Endpoints

### Create Accounts
```bash
# Create checking account
curl -X POST http://localhost:8082/api/checking-accounts \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"CHK001","customerId":"CUST001","initialBalance":1000.00}'

# Create saving account
curl -X POST http://localhost:8081/api/saving-accounts \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"SAV001","customerId":"CUST001","initialBalance":500.00}'
```

### Transfer Money
```bash
# Transfer from checking to saving
curl -X POST http://localhost:8083/api/transfers/checking-to-saving \
  -H "Content-Type: application/json" \
  -d '{"fromAccountNumber":"CHK001","toAccountNumber":"SAV001","amount":200.00}'

# Test error scenario (simulates failure and rollback)
curl -X POST http://localhost:8083/api/transfers/test-error \
  -H "Content-Type: application/json" \
  -d '{"fromAccountNumber":"CHK001","toAccountNumber":"SAV001","amount":100.00}'
```

## Testing Transaction Rollback

The system includes error simulation to test transaction rollback:

1. Make a transfer request with `simulateError: true`
2. The system will withdraw from the source account
3. Simulate an error before depositing to destination
4. Automatically rollback the withdrawal
5. Verify that the source account balance is restored

## Key Features

- **Distributed Transaction Management**: Implements compensation pattern for rollback
- **Error Simulation**: Built-in error simulation for testing
- **Service Discovery**: Uses Eureka for service registration
- **RESTful APIs**: Clean REST endpoints for all operations
- **Transaction Safety**: Automatic rollback on failures

## Database

Each service uses an in-memory H2 database. Access the H2 console at:
- SavingAccountService: http://localhost:8081/h2-console
- CheckingAccountService: http://localhost:8082/h2-console

JDBC URL: `jdbc:h2:mem:testdb`
Username: `sa`
Password: `password`
