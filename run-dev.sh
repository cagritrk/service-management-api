#!/bin/bash

# Service Management System - Local Development Startup Script
# Version: 1.0
# Description: Simplifies local development setup

# Check requirements
check_requirements() {
    echo "Checking system requirements..."

    # Check Java
    if ! command -v java &> /dev/null; then
        echo "[ERROR]: Java 24 is required but not found"
        exit 1
    fi

    # Check Maven
    if ! command -v mvn &> /dev/null; then
        echo "[ERROR]: Maven is required but not found"
        exit 1
    fi

    # Check MongoDB
    if ! pgrep -x "mongod" > /dev/null; then
        echo "! Warning: Application requires MongoDB"
        read -p "Are you running MongoDB with the correct credentials defined in application-dev.yaml? (y/n): " running_mongo
        if [ "$running_mongo" = "y" ]; then
            echo "[OK] MongoDB running on local (approved)"
        else
            echo "[FAILED] Application requires MongoDB to run"
            exit 1
        fi
    fi

    echo "[OK] All requirements satisfied"
}

# Run the application
run_application() {
    echo "Building and starting the application in development mode..."

    # Build with Maven
    mvn clean package -DskipTests || {
        echo "[ERROR] Build failed"
        exit 1
    }

    # Run with dev profile
    java -jar target/*.jar --spring.profiles.active=dev &

    # Wait for app to start
    sleep 10

    # Check if application started successfully
    if curl --output /dev/null --silent --head --fail http://localhost:8080/swagger-ui/index.html; then
        echo -e "\n✅ Application successfully running at:"
        echo -e "   - Local URL:    http://localhost:8080"
        echo -e "   - Swagger UI:   http://localhost:8080/swagger-ui.html"
    else
        echo "[FAILED] Failed to start application"
        exit 1
    fi
}

# Main execution
clear
echo "============================================"
echo "  Service Management System - Local Runner  "
echo "============================================"

check_requirements
run_application

# Keep the script running
echo -e "\nPress Ctrl+C to stop the application"
wait