
pipeline {
    agent any

    environment {
        DOCKERHUB_USER = 'shankarmurugaiyan'
        IMAGE_TAG = 'latest'
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Images') {
            steps {
                sh '''
                    docker compose -f "$COMPOSE_FILE" build
                '''
            }
        }

        stage('Docker Hub Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DH_TOKEN" | docker login \
                            --username "$DH_USER" \
                            --password-stdin
                    '''
                }
            }
        }

        stage('Push Images') {
            steps {
                sh '''
                    docker compose -f "$COMPOSE_FILE" push
                '''
            }
        }

        stage('Deploy Containers') {
            steps {
                sh '''
                    docker compose -f "$COMPOSE_FILE" pull
                    docker compose -f "$COMPOSE_FILE" up -d
                    docker compose -f "$COMPOSE_FILE" ps
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }

        success {
            echo 'Toy Store deployment completed!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
