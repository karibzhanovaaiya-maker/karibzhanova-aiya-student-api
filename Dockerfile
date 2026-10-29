# Multi-stage Dockerfile for the Student API by Karibzhanova Aiya
#
# Stage 1 (build):   full JDK + Gradle wrapper -> builds build/libs/app.jar
# Stage 2 (runtime): small JRE image that only contains the JAR
#
# Build:  docker build -t karibzhanova-aiya-student-api:v1.0-karibzhanova-aiya .

# ---------- Stage 1: build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy Gradle files first, so dependencies are cached in their own layer
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null

# Copy the source code and build the JAR (tests run in Jenkins, not here)
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre
LABEL org.opencontainers.image.title="karibzhanova-aiya-student-api" \
      org.opencontainers.image.authors="Karibzhanova Aiya"
WORKDIR /app

# Run as a normal user, not root
RUN useradd --system --create-home appuser
COPY --from=build /workspace/build/libs/app.jar app.jar
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
