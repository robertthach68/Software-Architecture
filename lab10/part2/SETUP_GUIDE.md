# Lab 10 Part 2 - Complete Setup Guide

## Prerequisites
- Java 11 or higher
- Maven
- Git
- GitHub account

## Step-by-Step Setup

### 1. Create GitHub Repository

1. Go to https://github.com/
2. Sign up for a free account if you don't have one
3. Click the "+" button in the upper right corner
4. Select "New repository"
5. Name your repository: `springcloud`
6. Add a description: "Spring Cloud Config Repository"
7. Select "Initialize this repository with a README"
8. Click "Create Repository"

### 2. Add Configuration Files to GitHub

In your GitHub repository, create these files:

#### ServiceA.yml
```yaml
greeting: Hello from ServiceA
```

#### ServiceB.yml
```yaml
greeting: Hello from ServiceB
```

#### application.yml
```yaml
message: Welcome to Spring Cloud Config
```

### 3. Update ConfigServer Configuration

1. Open `ConfigServer/src/main/resources/application.properties`
2. Replace `YOUR_USERNAME` with your actual GitHub username:
   ```properties
   spring.cloud.config.server.git.uri=https://github.com/YOUR_USERNAME/springcloud.git
   ```

### 4. Build and Run Services

#### Option 1: Using the provided scripts
```bash
# Start all services
./run_services.sh

# Test services
./test_services.sh

# Stop all services
./stop_services.sh
```

#### Option 2: Manual startup
```bash
# Terminal 1 - ConfigServer
cd ConfigServer
./mvnw spring-boot:run

# Terminal 2 - ServiceA
cd ServiceAApplication
./mvnw spring-boot:run

# Terminal 3 - ServiceB
cd ServiceBApplication
./mvnw spring-boot:run
```

### 5. Test the Services

#### Test ConfigServer
- http://localhost:8888/ServiceA/default
- http://localhost:8888/ServiceB/default

#### Test Service Applications
- http://localhost:8081/ (ServiceA)
- http://localhost:8082/ (ServiceB)

Expected responses:
- ServiceA: "Welcome to Spring Cloud Config , Hello from ServiceA"
- ServiceB: "Welcome to Spring Cloud Config , Hello from ServiceB"

### 6. Test Configuration Changes

1. Modify the configuration files in your GitHub repository
2. Restart the services
3. Verify that the changes are reflected in the service responses

## Troubleshooting

### Common Issues

1. **ConfigServer can't connect to GitHub**
   - Check the GitHub repository URL in application.properties
   - Ensure the repository is public or you have proper authentication

2. **Services can't connect to ConfigServer**
   - Ensure ConfigServer is running on port 8888
   - Check the ConfigServer logs for errors

3. **Configuration not loading**
   - Verify the configuration files exist in GitHub
   - Check the service names match the configuration file names
   - Restart the services after making changes

### Logs

Check the log files for debugging:
- `ConfigServer.log`
- `ServiceAApplication.log`
- `ServiceBApplication.log`

## Architecture

```
GitHub Repository (springcloud)
├── ServiceA.yml (ServiceA-specific config)
├── ServiceB.yml (ServiceB-specific config)
└── application.yml (Shared config)

ConfigServer (Port 8888)
├── Fetches config from GitHub
└── Serves config to clients

ServiceA (Port 8081)          ServiceB (Port 8082)
├── Connects to ConfigServer  ├── Connects to ConfigServer
├── Loads ServiceA.yml        ├── Loads ServiceB.yml
└── Loads application.yml     └── Loads application.yml
```

## Key Features Demonstrated

- **Centralized Configuration**: All configuration stored in GitHub
- **Service-specific Configuration**: Each service can have its own config file
- **Shared Configuration**: Common configuration across services
- **Dynamic Configuration**: Changes in GitHub are picked up on service restart
- **Spring Cloud Config**: Automatic configuration loading and management

