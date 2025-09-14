# Lab 10 Part 2: Spring Cloud Config Server with GitHub

This lab demonstrates how to use Spring Cloud Config Server with a GitHub repository to manage configuration for multiple services.

## Setup Instructions

### 1. Create GitHub Repository

1. Go to https://github.com/ and sign up for a free account
2. Create a new repository named `springcloud`
3. Add a short description
4. Select "Initialize this repository with a README"
5. Click "Create Repository"

### 2. Add Configuration Files to GitHub

Create the following files in your GitHub repository:

#### ServiceA.yml
```yaml
greeting: Hello from ServiceA
```

#### ServiceB.yml
```yaml
greeting: Hello from ServiceB
```

#### application.yml (shared configuration)
```yaml
message: Welcome to Spring Cloud Config
```

### 3. Update ConfigServer Configuration

Update the `ConfigServer/src/main/resources/application.properties` file:

```properties
server.port=8888
spring.application.name=ConfigServer

# Replace YOUR_USERNAME with your actual GitHub username
spring.cloud.config.server.git.uri=https://github.com/YOUR_USERNAME/springcloud.git
spring.cloud.config.server.git.default-label=main
spring.cloud.config.server.git.clone-on-start=true
```

### 4. Running the Services

#### Start ConfigServer
```bash
cd ConfigServer
./mvnw spring-boot:run
```

#### Start ServiceAApplication
```bash
cd ServiceAApplication
./mvnw spring-boot:run
```

#### Start ServiceBApplication
```bash
cd ServiceBApplication
./mvnw spring-boot:run
```

### 5. Testing the Services

#### Test ConfigServer
- http://localhost:8888/ServiceA/default
- http://localhost:8888/ServiceB/default

#### Test ServiceA
- http://localhost:8081/

#### Test ServiceB
- http://localhost:8082/

### 6. Testing Configuration Changes

1. Modify the configuration files in your GitHub repository
2. Restart the services to pick up the new configuration
3. Verify that the changes are reflected in the service responses

## Project Structure

```
lab10/part2/
├── ConfigServer/
│   ├── src/main/java/com/example/configserver/
│   │   └── ConfigServerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── ServiceAApplication/
│   ├── src/main/java/com/example/servicea/
│   │   ├── ServiceAApplication.java
│   │   └── ServiceAController.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── ServiceBApplication/
│   ├── src/main/java/com/example/serviceb/
│   │   ├── ServiceBApplication.java
│   │   └── ServiceBController.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── README.md
```

## Key Features

- **ConfigServer**: Centralized configuration management
- **Git Integration**: Configuration stored in GitHub repository
- **Service-specific configs**: ServiceA.yml and ServiceB.yml
- **Shared configuration**: application.yml for common settings
- **@RefreshScope**: Enables configuration refresh without restart
- **Spring Cloud Config Client**: Automatic configuration loading

