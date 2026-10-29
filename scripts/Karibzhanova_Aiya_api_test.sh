#!/bin/bash
# CRUD test of the Student API with curl
# Author: Karibzhanova Aiya
# Usage:  ./scripts/Karibzhanova_Aiya_api_test.sh [base-url]

BASE_URL="${1:-http://localhost:8080}"
API="$BASE_URL/api/students"
JSON="Content-Type: application/json"

step() {
    echo
    echo "=== $1 ==="
}

step "0. Application info"
curl -s "$BASE_URL/"
echo

step "1. Create a student (POST)"
RESPONSE=$(curl -s -w "\nHTTP %{http_code}" -X POST "$API" -H "$JSON" \
    -d '{"firstName":"Aiya","lastName":"Karibzhanova","email":"aiya.karibzhanova@iitu.edu.kz","major":"Software Engineering","course":3}')
echo "$RESPONSE"
ID=$(echo "$RESPONSE" | head -1 | sed -E 's/.*"id":([0-9]+).*/\1/')
echo "New student id: $ID"

step "2. Get all students (GET)"
curl -s -w "\nHTTP %{http_code}\n" "$API"

step "3. Get one student by id (GET /$ID)"
curl -s -w "\nHTTP %{http_code}\n" "$API/$ID"

step "4. Update the student (PUT /$ID)"
curl -s -w "\nHTTP %{http_code}\n" -X PUT "$API/$ID" -H "$JSON" \
    -d '{"firstName":"Aiya","lastName":"Karibzhanova","email":"aiya.karibzhanova@iitu.edu.kz","major":"DevOps Engineering","course":4}'

step "5. Delete the student (DELETE /$ID)"
curl -s -w "HTTP %{http_code}\n" -X DELETE "$API/$ID"

step "6. Get a student that does not exist (GET /99999)"
curl -s -w "\nHTTP %{http_code}\n" "$API/99999"
