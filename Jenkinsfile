// CI pipeline for the Student API by Karibzhanova Aiya
// Stages: Checkout -> Build -> Test -> Docker Build -> Verify
// If any stage fails, the next stages do not run and the build is marked FAILED.

pipeline {
    agent any

    environment {
        STUDENT_NAME = 'Karibzhanova Aiya'
        IMAGE_NAME   = 'karibzhanova-aiya-student-api'
        IMAGE_TAG    = 'v1.1-karibzhanova-aiya'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    // Check GitHub for new commits every 2 minutes
    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pipeline of student: ${STUDENT_NAME}"
                checkout scm
                sh 'git log --oneline -3'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble --no-daemon'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} ."
            }
        }

        stage('Verify') {
            steps {
                sh "docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} --format 'Image OK: {{.RepoTags}} size={{.Size}} bytes created={{.Created}}'"
                sh "docker images ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo "SUCCESS: ${IMAGE_NAME}:${IMAGE_TAG} built for ${STUDENT_NAME}"
        }
        failure {
            echo "FAILED: pipeline of ${STUDENT_NAME} stopped. Open the red stage to see the error."
        }
    }
}
