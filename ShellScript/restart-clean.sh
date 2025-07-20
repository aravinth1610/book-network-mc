#!/bin/bash

echo "🔄 Stopping and removing all containers..."
docker-compose down --volumes --remove-orphans

echo "🧹 Pruning unused containers..."
docker container prune -f

echo "🧼 Pruning unused volumes (optional)..."
docker volume prune -f


