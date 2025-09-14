#!/bin/bash

echo "Lab 9 Part 1: Downloading and Starting Zipkin"
echo "=============================================="

# Create zipkin directory
mkdir -p zipkin
cd zipkin

# Check if zipkin.jar already exists
if [ -f "zipkin.jar" ]; then
    echo "Zipkin JAR file already exists."
else
    echo "Downloading Zipkin..."
    echo "Note: The Google Drive link requires manual download."
    echo "Please download from: https://drive.google.com/file/d/1WxBvlwhiAZMSkZxnUIlKoJ3jSjtZiLPH/view?usp=sharing"
    echo "Place the downloaded file as 'zipkin.jar' in the zipkin directory."
    echo ""
    echo "Alternative: Download from official source:"
    echo "curl -L https://search.maven.org/remotecontent?filepath=io/zipkin/zipkin-server/2.23.16/zipkin-server-2.23.16-exec.jar -o zipkin.jar"
    echo ""
    
    # Try to download from Maven Central as alternative
    echo "Attempting to download from Maven Central..."
    if curl -L "https://search.maven.org/remotecontent?filepath=io/zipkin/zipkin-server/2.23.16/zipkin-server-2.23.16-exec.jar" -o zipkin.jar; then
        echo "Successfully downloaded Zipkin from Maven Central!"
    else
        echo "Failed to download from Maven Central."
        echo "Please manually download the zipkin.jar file and place it in the zipkin directory."
        exit 1
    fi
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 8 or higher and try again"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 8 ]; then
    echo "Error: Java 8 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version check passed."
echo ""

# Start Zipkin
echo "Starting Zipkin..."
echo "Zipkin will be available at: http://localhost:9411/zipkin"
echo "Press Ctrl+C to stop Zipkin"
echo ""

java -jar zipkin.jar

