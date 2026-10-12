pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Docker') {
            steps {
                bat 'docker --version'
                bat 'docker compose version'
            }
        }

        stage('Build Images') {
            steps {
                bat 'docker compose -f "%COMPOSE_FILE%" build'
            }
        }

        stage('Docker Hub Login and Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-cred',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_TOKEN'
                    )
                ]) {
                    // Changed to double quotes (""") so Jenkins environment variables inject properly
                    powershell """
                        \$ErrorActionPreference = "Stop"
                        
                        # Validate credentials have been successfully loaded by Jenkins
                        if ([string]::IsNullOrWhiteSpace("${env.DH_USER}")) {
                            throw "Docker Hub username is empty"
                        }
                        if ([string]::IsNullOrWhiteSpace("${env.DH_TOKEN}")) {
                            throw "Docker Hub token is empty"
                        }

                        # Securely pipe the token to stdin using a clean Windows-native method
                        "${env.DH_TOKEN}" | docker login --username "${env.DH_USER}" --password-stdin
                    """
                    
                    // Push the newly built images using the existing compose configuration
                    bat 'docker compose -f "%COMPOSE_FILE%" push'
                }
            }
        }

        stage('Deploy Containers') {
            steps {
                bat 'docker compose -f "%COMPOSE_FILE%" pull'
                bat 'docker compose -f "%COMPOSE_FILE%" up -d'
                bat 'docker compose -f "%COMPOSE_FILE%" ps'
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }

        success {
            echo 'Toy Store deployment completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check the first error in Console Output.'
        }
    }
}