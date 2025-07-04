# Book Application with JMS Messaging

This project demonstrates JMS messaging between a BookApplication (sender) and BookReceiver (listener) using ActiveMQ.

## Project Structure

### BookApplication (Part 1)
- **Location**: `lab2/part1/BookApplication/`
- **Purpose**: REST API for managing books with JMS messaging
- **Features**:
  - CRUD operations for books (Add, Update, Delete, Get)
  - Sends JMS messages when books are added, updated, or deleted
  - Uses ActiveMQ for message queuing

### BookReceiver (Part 3)
- **Location**: `lab2/part3/BookReceiver/`
- **Purpose**: JMS message listener for book operations
- **Features**:
  - Listens to `bookQueue` for book messages
  - Prints received book messages with operation details
  - Provides status endpoint

## Changes Made

### BookApplication Changes:
1. **Added JMS Dependencies** (`build.gradle`):
   - `spring-boot-starter-activemq`
   - `jackson-databind`

2. **Created JmsSender** (`JmsSender.java`):
   - Sends book messages with operation type
   - Uses JSON serialization
   - Sends to `bookQueue`

3. **Updated BookService** (`BookService.java`):
   - Injected JmsSender
   - Sends messages on ADD, UPDATE, DELETE operations

4. **Updated Main Application** (`BookApplication.java`):
   - Added `@EnableJms` annotation

5. **Added JMS Configuration** (`application.properties`):
   - ActiveMQ broker configuration

### BookReceiver Changes:
1. **Added Jackson Dependency** (`pom.xml`):
   - For JSON deserialization

2. **Created Book Domain** (`Book.java`):
   - Same structure as BookApplication Book class

3. **Created BookMessage** (`BookMessage.java`):
   - Contains book and operation information

4. **Created BookJmsListener** (`BookJmsListener.java`):
   - Listens to `bookQueue`
   - Deserializes JSON messages
   - Prints received messages

5. **Updated JmsSender** (`JmsSender.java`):
   - Changed queue from `testQueue` to `bookQueue`

6. **Added Status Controller** (`BookReceiverController.java`):
   - Provides status endpoint

## How to Run

### Prerequisites:
1. ActiveMQ server running on `localhost:61616`
2. Java 11+ and Maven/Gradle

### Start BookReceiver:
```bash
cd lab2/part3/BookReceiver
mvn spring-boot:run
```

### Start BookApplication:
```bash
cd lab2/part1/BookApplication
./gradlew bootRun
```

## Testing

### Test BookReceiver Status:
```bash
curl http://localhost:8080/status
```

### Test Book Operations:
```bash
# Add a book
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"123","author":"John Doe","title":"Test Book","price":29.99}'

# Update a book
curl -X PUT http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"123","author":"John Doe","title":"Updated Book","price":39.99}'

# Delete a book
curl -X DELETE http://localhost:8081/api/books/123
```

## Expected Output

When you perform book operations, you should see messages in the BookReceiver console:

```
Received Book Message:
Operation: ADDED
Book: Book{isbn='123', author='John Doe', title='Test Book', price=29.99}
----------------------------------------
```

## Message Format

The JMS messages contain:
- **Operation**: ADDED, UPDATED, or DELETED
- **Book**: Complete book object with ISBN, author, title, and price

## Queue Configuration

- **Queue Name**: `bookQueue`
- **Broker**: ActiveMQ on `localhost:61616`
- **Authentication**: admin/admin 