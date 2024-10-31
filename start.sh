#!/bin/bash

# Check if environment argument is provided
if [ -z "$1" ]; then
    echo "Usage: ./start.sh <environment> [command]"
    echo "Environments: dev, test, prod"
    echo "Commands: up, down, logs, build"
    exit 1
fi

# Set environment
export SPRING_PROFILE=$1

# Load environment variables
if [ -f ".env.$SPRING_PROFILE" ]; then
    while IFS='=' read -r key value; do
        # Skip comments and empty lines
        if [[ $key != \#* ]] && [ ! -z "$key" ]; then
            # Remove leading/trailing whitespace and comments from key
            key=$(echo "$key" | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')

            # Remove leading/trailing whitespace and comments from value
            value=$(echo "$value" | sed 's/[[:space:]]*#.*$//;s/^[[:space:]]*//;s/[[:space:]]*$//')

            # Only export if both key and value are non-empty
            if [ ! -z "$key" ] && [ ! -z "$value" ]; then
                # Export without any extra spaces
                export "${key}=${value}"
            fi
        fi
    done < ".env.$SPRING_PROFILE"
fi

# Default command to 'up' if not specified
COMMAND=${2:-up}

# Common docker-compose files
COMPOSE_FILES="-f docker-compose.yml -f docker-compose.yml"

# Execute docker-compose command
case $COMMAND in
    up)
        docker-compose $COMPOSE_FILES up -d
        ;;
    down)
        docker-compose $COMPOSE_FILES down
        ;;
    logs)
        docker-compose $COMPOSE_FILES logs -f
        ;;
    build)
        docker-compose $COMPOSE_FILES build --no-cache
        ;;
    *)
        echo "Unknown command: $COMMAND"
        exit 1
        ;;
esac