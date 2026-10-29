#!/bin/bash
# Builds and starts the Jenkins container for the Student API
# Author: Karibzhanova Aiya
# Usage:  ./jenkins/start-jenkins.sh      then open http://localhost:8081

set -e
cd "$(dirname "$0")"

# Admin password is read from the project .env file (never committed)
source ../.env

# Group id of the Docker socket, so the jenkins user may use Docker
DOCKER_GID=$(docker run --rm -v /var/run/docker.sock:/s alpine stat -c %g /s)

docker build -t karibzhanova-aiya-jenkins:lts .

docker rm -f karibzhanova-aiya-jenkins 2>/dev/null || true
docker run -d \
    --name karibzhanova-aiya-jenkins \
    --restart unless-stopped \
    -p 8081:8080 \
    -v karibzhanova-aiya-jenkins-home:/var/jenkins_home \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --group-add "$DOCKER_GID" \
    -e JENKINS_ADMIN_USER=admin \
    -e JENKINS_ADMIN_PASSWORD="$JENKINS_ADMIN_PASSWORD" \
    karibzhanova-aiya-jenkins:lts

echo "Jenkins is starting on http://localhost:8081 (user: admin)"
