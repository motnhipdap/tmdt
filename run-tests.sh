#!/bin/bash

# Script to run unit tests for authorization module

echo "======================================"
echo "Running Authorization Module Tests"
echo "======================================"
echo ""

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}$1${NC}"
}

# Make mvnw executable
chmod +x mvnw

# Parse command line arguments
if [ "$1" == "all" ]; then
    print_info "Running all tests..."
    ./mvnw test
elif [ "$1" == "auth" ]; then
    print_info "Running authorization module tests..."
    ./mvnw test -Dtest=com.dev.dungcony.modules.authorization.**
elif [ "$1" == "service" ]; then
    print_info "Running service layer tests..."
    ./mvnw test -Dtest=com.dev.dungcony.modules.authorization.services.impl.**
elif [ "$1" == "controller" ]; then
    print_info "Running controller layer tests..."
    ./mvnw test -Dtest=com.dev.dungcony.modules.authorization.controllers.**
elif [ -n "$1" ]; then
    print_info "Running specific test: $1"
    ./mvnw test -Dtest="$1"
else
    echo "Usage: ./run-tests.sh [option]"
    echo ""
    echo "Options:"
    echo "  all         - Run all tests in the project"
    echo "  auth        - Run all authorization module tests"
    echo "  service     - Run service layer tests only"
    echo "  controller  - Run controller layer tests only"
    echo "  <TestName>  - Run specific test class (e.g., AuthServiceImplTest)"
    echo ""
    echo "Examples:"
    echo "  ./run-tests.sh all"
    echo "  ./run-tests.sh auth"
    echo "  ./run-tests.sh service"
    echo "  ./run-tests.sh AuthServiceImplTest"
    echo "  ./run-tests.sh AuthServiceImplTest#login_Success"
    exit 1
fi

# Check exit code
if [ $? -eq 0 ]; then
    print_success ""
    print_success "✓ All tests passed!"
else
    echo ""
    echo "✗ Some tests failed. Check the output above for details."
    exit 1
fi
