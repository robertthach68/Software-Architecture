# Part 3: Database Design Analysis for Order Storage

## Order Data to Store

**Order Information:**
- Order Number: 122435
- Order Date: 11/09/2021
- Total Price: $5160.00

**Customer Information:**
- Name: Frank Brown
- Email: fbrown@gmail.com
- Phone: 0623156543

**Order Items:**
1. 2 × iPhone 12 (Product #A546) @ $980.00 each
2. 4 × Samsung Galaxy 12S (Product #S333) @ $800.00 each

---

## 1. Relational Database (SQL) Design

### Tables Structure

#### Customers Table
```sql
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL
);
```

**Sample Data:**
```
| customer_id | name        | email           | phone      |
|-------------|-------------|-----------------|------------|
| 1           | Frank Brown | fbrown@gmail.com| 0623156543 |
```

#### Products Table
```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_number VARCHAR(20) UNIQUE NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);
```

**Sample Data:**
```
| product_id | product_number | product_name      | price  |
|------------|----------------|-------------------|--------|
| 1          | A546          | iPhone 12         | 980.00 |
| 2          | S333          | Samsung Galaxy 12S| 800.00 |
```

#### Orders Table
```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(20) UNIQUE NOT NULL,
    order_date DATE NOT NULL,
    customer_id INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
```

**Sample Data:**
```
| order_id | order_number | order_date | customer_id | total_price |
|----------|--------------|------------|-------------|-------------|
| 1        | 122435       | 2021-09-11 | 1           | 5160.00     |
```

#### Order_Items Table
```sql
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

**Sample Data:**
```
| order_item_id | order_id | product_id | quantity | unit_price | line_total |
|---------------|----------|------------|----------|------------|------------|
| 1             | 1        | 1          | 2        | 980.00     | 1960.00    |
| 2             | 1        | 2          | 4        | 800.00     | 3200.00    |
```

### Relationships
- **Customers** (1) → (N) **Orders**
- **Orders** (1) → (N) **Order_Items**
- **Products** (1) → (N) **Order_Items**

### Query Example
```sql
SELECT 
    o.order_number,
    o.order_date,
    c.name as customer_name,
    c.email as customer_email,
    c.phone as customer_phone,
    o.total_price,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.line_total
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE o.order_number = '122435';
```

---

## 2. MongoDB (NoSQL Document) Design

### Collections Structure

#### Customers Collection
```json
{
  "_id": ObjectId("..."),
  "customer_id": 1,
  "name": "Frank Brown",
  "email": "fbrown@gmail.com",
  "phone": "0623156543"
}
```

#### Products Collection
```json
{
  "_id": ObjectId("..."),
  "product_id": 1,
  "product_number": "A546",
  "product_name": "iPhone 12",
  "price": 980.00
}
```

#### Orders Collection (Embedded Design)
```json
{
  "_id": ObjectId("..."),
  "order_number": "122435",
  "order_date": "2021-09-11",
  "customer": {
    "customer_id": 1,
    "name": "Frank Brown",
    "email": "fbrown@gmail.com",
    "phone": "0623156543"
  },
  "total_price": 5160.00,
  "items": [
    {
      "product_id": 1,
      "product_number": "A546",
      "product_name": "iPhone 12",
      "quantity": 2,
      "unit_price": 980.00,
      "line_total": 1960.00
    },
    {
      "product_id": 2,
      "product_number": "S333",
      "product_name": "Samsung Galaxy 12S",
      "quantity": 4,
      "unit_price": 800.00,
      "line_total": 3200.00
    }
  ]
}
```

### Alternative Design (Normalized)
```json
// Orders Collection
{
  "_id": ObjectId("..."),
  "order_number": "122435",
  "order_date": "2021-09-11",
  "customer_id": 1,
  "total_price": 5160.00
}

// Order_Items Collection
{
  "_id": ObjectId("..."),
  "order_id": ObjectId("..."),
  "product_id": 1,
  "quantity": 2,
  "unit_price": 980.00,
  "line_total": 1960.00
}
```

### Query Example
```javascript
// Find order with embedded data
db.orders.findOne({ "order_number": "122435" })

// Find order with customer details
db.orders.aggregate([
  { $match: { "order_number": "122435" } },
  { $lookup: { from: "customers", localField: "customer_id", foreignField: "customer_id", as: "customer" } }
])
```

---

## 3. Cassandra (Wide-Column) Design

### Tables Structure

#### Orders Table
```sql
CREATE TABLE orders (
    order_number text,
    order_date date,
    customer_name text,
    customer_email text,
    customer_phone text,
    total_price decimal,
    PRIMARY KEY (order_number)
);
```

**Sample Data:**
```
| order_number | order_date | customer_name | customer_email | customer_phone | total_price |
|--------------|------------|---------------|----------------|----------------|-------------|
| 122435       | 2021-09-11 | Frank Brown   | fbrown@gmail.com| 0623156543    | 5160.00     |
```

#### Order_Items Table
```sql
CREATE TABLE order_items (
    order_number text,
    product_number text,
    product_name text,
    quantity int,
    unit_price decimal,
    line_total decimal,
    PRIMARY KEY (order_number, product_number)
);
```

**Sample Data:**
```
| order_number | product_number | product_name      | quantity | unit_price | line_total |
|--------------|---------------|-------------------|----------|------------|------------|
| 122435       | A546          | iPhone 12         | 2        | 980.00     | 1960.00    |
| 122435       | S333          | Samsung Galaxy 12S| 4        | 800.00     | 3200.00    |
```

#### Products Table
```sql
CREATE TABLE products (
    product_number text,
    product_name text,
    price decimal,
    PRIMARY KEY (product_number)
);
```

**Sample Data:**
```
| product_number | product_name      | price  |
|---------------|-------------------|--------|
| A546          | iPhone 12         | 980.00 |
| S333          | Samsung Galaxy 12S| 800.00 |
```

### Alternative Design (Denormalized for Query Performance)
```sql
CREATE TABLE order_details (
    order_number text,
    order_date date,
    customer_name text,
    customer_email text,
    customer_phone text,
    product_number text,
    product_name text,
    quantity int,
    unit_price decimal,
    line_total decimal,
    total_price decimal,
    PRIMARY KEY (order_number, product_number)
);
```

**Sample Data:**
```
| order_number | order_date | customer_name | customer_email | customer_phone | product_number | product_name | quantity | unit_price | line_total | total_price |
|--------------|------------|---------------|----------------|----------------|----------------|--------------|----------|------------|------------|-------------|
| 122435       | 2021-09-11 | Frank Brown   | fbrown@gmail.com| 0623156543    | A546          | iPhone 12    | 2        | 980.00     | 1960.00    | 5160.00     |
| 122435       | 2021-09-11 | Frank Brown   | fbrown@gmail.com| 0623156543    | S333          | Samsung Galaxy 12S| 4    | 800.00     | 3200.00    | 5160.00     |
```

### Query Example
```sql
-- Get order details
SELECT * FROM order_details WHERE order_number = '122435';

-- Get order summary
SELECT order_number, order_date, customer_name, total_price 
FROM orders WHERE order_number = '122435';
```

---

## 4. Neo4j (Graph Database) Design

### Nodes and Relationships

#### Customer Node
```cypher
CREATE (c:Customer {
  customer_id: 1,
  name: "Frank Brown",
  email: "fbrown@gmail.com",
  phone: "0623156543"
})
```

#### Order Node
```cypher
CREATE (o:Order {
  order_number: "122435",
  order_date: "2021-09-11",
  total_price: 5160.00
})
```

#### Product Nodes
```cypher
CREATE (p1:Product {
  product_id: 1,
  product_number: "A546",
  product_name: "iPhone 12",
  price: 980.00
})

CREATE (p2:Product {
  product_id: 2,
  product_number: "S333",
  product_name: "Samsung Galaxy 12S",
  price: 800.00
})
```

#### OrderItem Nodes
```cypher
CREATE (oi1:OrderItem {
  quantity: 2,
  unit_price: 980.00,
  line_total: 1960.00
})

CREATE (oi2:OrderItem {
  quantity: 4,
  unit_price: 800.00,
  line_total: 3200.00
})
```

### Relationships
```cypher
-- Customer placed order
MATCH (c:Customer {customer_id: 1})
MATCH (o:Order {order_number: "122435"})
CREATE (c)-[:PLACED]->(o)

-- Order contains items
MATCH (o:Order {order_number: "122435"})
MATCH (oi1:OrderItem {line_total: 1960.00})
MATCH (oi2:OrderItem {line_total: 3200.00})
CREATE (o)-[:CONTAINS]->(oi1)
CREATE (o)-[:CONTAINS]->(oi2)

-- Items are for specific products
MATCH (oi1:OrderItem {line_total: 1960.00})
MATCH (oi2:OrderItem {line_total: 3200.00})
MATCH (p1:Product {product_number: "A546"})
MATCH (p2:Product {product_number: "S333"})
CREATE (oi1)-[:FOR_PRODUCT]->(p1)
CREATE (oi2)-[:FOR_PRODUCT]->(p2)
```

### Complete Graph Structure
```
(Customer)-[:PLACED]->(Order)-[:CONTAINS]->(OrderItem1)-[:FOR_PRODUCT]->(Product1)
(Customer)-[:PLACED]->(Order)-[:CONTAINS]->(OrderItem2)-[:FOR_PRODUCT]->(Product2)
```

### Alternative Design (Embedded Properties)
```cypher
CREATE (o:Order {
  order_number: "122435",
  order_date: "2021-09-11",
  customer: {
    name: "Frank Brown",
    email: "fbrown@gmail.com",
    phone: "0623156543"
  },
  items: [
    {
      product_number: "A546",
      product_name: "iPhone 12",
      quantity: 2,
      unit_price: 980.00,
      line_total: 1960.00
    },
    {
      product_number: "S333",
      product_name: "Samsung Galaxy 12S",
      quantity: 4,
      unit_price: 800.00,
      line_total: 3200.00
    }
  ],
  total_price: 5160.00
})
```

### Query Example
```cypher
// Get complete order with relationships
MATCH (c:Customer)-[:PLACED]->(o:Order {order_number: "122435"})-[:CONTAINS]->(oi:OrderItem)-[:FOR_PRODUCT]->(p:Product)
RETURN c.name as customer_name, o.order_date, o.total_price, p.product_name, oi.quantity, oi.line_total

// Get order with embedded data
MATCH (o:Order {order_number: "122435"})
RETURN o
```

---

## Summary of Database Design Approaches

### **Relational Database (SQL)**
**Pros:**
- ACID compliance
- Normalized structure
- Complex queries with JOINs
- Data integrity constraints
- Mature ecosystem

**Cons:**
- Fixed schema
- Performance issues with complex joins
- Horizontal scaling challenges

**Best for:** Transactional systems, complex reporting, financial applications

### **MongoDB (Document)**
**Pros:**
- Flexible schema
- Embedded documents
- Horizontal scaling
- JSON-like structure
- Fast reads for document-based queries

**Cons:**
- No ACID compliance across documents
- Limited JOIN capabilities
- Data duplication

**Best for:** Content management, real-time analytics, mobile applications

### **Cassandra (Wide-Column)**
**Pros:**
- High availability
- Linear scalability
- Fast writes
- No single point of failure
- Tunable consistency

**Cons:**
- Limited query flexibility
- Eventual consistency
- Complex data modeling
- No JOINs

**Best for:** Time-series data, high-write applications, IoT data

### **Neo4j (Graph)**
**Pros:**
- Complex relationship queries
- Flexible schema
- Intuitive data modeling
- Graph algorithms
- Pattern matching

**Cons:**
- Limited scalability
- Complex for simple queries
- Higher resource requirements
- Smaller ecosystem

**Best for:** Social networks, recommendation systems, fraud detection, knowledge graphs

---

## CAP Theorem Considerations

Each database type makes different trade-offs in the CAP theorem:

- **Relational (SQL)**: CP (Consistency + Partition Tolerance)
- **MongoDB**: AP (Availability + Partition Tolerance)
- **Cassandra**: AP (Availability + Partition Tolerance)
- **Neo4j**: CP (Consistency + Partition Tolerance)

The choice of database depends on the specific requirements of the application, including:
- Data consistency needs
- Query complexity
- Scalability requirements
- Performance expectations
- Development team expertise 