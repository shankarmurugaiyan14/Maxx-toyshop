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
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-cred', 
                    usernameVariable: 'DH_USER', 
                    passwordVariable: 'DH_TOKEN'
                )]) {
                    bat '''
                        echo %DH_TOKEN% | docker login -u %DH_USER% --password-stdin
                        docker compose -f %COMPOSE_FILES% push
                    '''
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