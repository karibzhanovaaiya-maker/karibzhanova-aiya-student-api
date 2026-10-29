# Student API

Spring Boot REST API for managing university students, with PostgreSQL, Docker and Jenkins, by Karibzhanova Aiya.

Author: **Karibzhanova Aiya**

## Tech stack

- Java 21, Spring Boot 4, Gradle (wrapper)
- PostgreSQL 16
- Docker, Docker Compose
- Jenkins

## Project structure

```
karibzhanova-aiya-student-api/
├── scripts/            Bash scripts (system report)
├── logs/               Script output logs
├── backups/            Backup copies of files
├── src/                Java source code and tests
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── build.gradle
└── README.md
```

## Run tests

```bash
./gradlew test
```

## API endpoints

| Method | URL                  | Description          | Success code |
|--------|----------------------|----------------------|--------------|
| GET    | /api/students        | Get all students     | 200          |
| GET    | /api/students/{id}   | Get one student      | 200 / 404    |
| POST   | /api/students        | Create a student     | 201          |
| PUT    | /api/students/{id}   | Update a student     | 200 / 404    |
| DELETE | /api/students/{id}   | Delete a student     | 204 / 404    |

## Change log

- **Karibzhanova Aiya, feature/karibzhanova-aiya-crud:** the create and update
  operations now check that the email is unique. A duplicate email returns
  `409 Conflict` with a clear JSON message instead of a database error.

## CI/CD with Jenkins

Jenkins job **Karibzhanova_Aiya_Student_API** runs the `Jenkinsfile`:

1. **Checkout**: gets the code from GitHub (read-only deploy key in Jenkins Credentials)
2. **Build**: compiles the application with the Gradle wrapper
3. **Test**: runs JUnit tests; the pipeline stops if any test fails
4. **Docker Build**: builds `karibzhanova-aiya-student-api:v1.1-karibzhanova-aiya`
5. **Verify**: checks that the image exists and prints its size

Jenkins checks GitHub for new commits every 2 minutes (`pollSCM`) and starts automatically.

Start Jenkins locally: `./jenkins/start-jenkins.sh`, then open http://localhost:8081
