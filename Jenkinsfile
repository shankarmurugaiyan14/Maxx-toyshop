pipeline {
    // Defines where the pipeline runs. 'any' allows it to run on any available Jenkins agent.
    agent any

    // Pipeline options configuration
    options {
        // Disables Jenkins from automatically checking out the repository before stages begin,
        // allowing us to explicitly handle checkout inside the 'Checkout' stage.
        skipDefaultCheckout(true)
    }

    // Global environment variables available throughout all pipeline stages
    environment {
        // Path to the Docker Compose definition file
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {

        // ==========================================
        // STAGE 1: CHECKOUT SOURCE CODE
        // ==========================================
        stage('Checkout') {
            steps {
                echo '=== STAGE: Checking out project code from Git repository ==='
                
                // Clones and pulls the latest source code from the configured repository branch
                checkout scm
            }
        }

        // ==========================================
        // STAGE 2: VALIDATE RUNTIME DEPENDENCIES
        // ==========================================
        stage('Check Docker') {
            steps {
                echo '=== STAGE: Verifying Docker CLI and Compose installation ==='
                
                // Prints the installed Docker daemon/client engine version to console logs
                bat 'docker --version'
                
                // Prints the installed Docker Compose plugin version to console logs
                bat 'docker compose version'
            }
        }

        // ==========================================
        // STAGE 3: BUILD APPLICATION DOCKER IMAGES
        // ==========================================
        stage('Build Images') {
            steps {
                echo '=== STAGE: Building application container images ==='
                
                // Compiles and builds all microservice images listed in docker-compose.yml
                // Uses the environment variable %COMPOSE_FILE% to ensure clean file resolution
                bat 'docker compose -f "%COMPOSE_FILE%" build'
            }
        }

        // ==========================================
        // STAGE 4: DOCKER HUB AUTHENTICATION & PUSH
        // ==========================================
        stage('Docker Hub Login and Push') {
            steps {
                echo '=== STAGE: Authenticating with Docker Hub registry and pushing images ==='
                
                // Securely pulls stored credentials from Jenkins Credential Store:
                // - credentialsId: The unique ID assigned in Jenkins Credentials management
                // - usernameVariable: Sets DH_USER variable containing Docker Hub username
                // - passwordVariable: Sets DH_TOKEN variable containing Docker Hub Personal Access Token (PAT)
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-cred', 
                    usernameVariable: 'DH_USER', 
                    passwordVariable: 'DH_TOKEN'
                )]) {
                    bat '''
                        @echo off
                        echo Authenticating with Docker Hub...
                        
                        :: Using '<nul set /p' sends the token directly to standard input without adding
                        :: an extra trailing newline or whitespace that causes authentication rejection.
                        <nul set /p="%DH_TOKEN%" | docker login -u %DH_USER% --password-stdin
                        
                        :: Checks the exit code. If login failed, terminate immediately with an error.
                        if %ERRORLEVEL% neq 0 (
                            echo [ERROR] Docker authentication failed with exit code %ERRORLEVEL%.
                            exit /b %ERRORLEVEL%
                        )
                        
                        echo [SUCCESS] Docker Hub login successful!
                        echo Pushing built images to Docker Hub registry...
                        
                        :: Pushes all tagged services to the remote Docker repository
                        docker compose -f "%COMPOSE_FILE%" push
                    '''
                }
            }
        }

        // ==========================================
        // STAGE 5: RUN & DEPLOY CONTAINERS
        // ==========================================
        stage('Deploy Containers') {
            steps {
                echo '=== STAGE: Deploying containers locally ==='
                
                // Pulls updated base or service images from the registry
                bat 'docker compose -f "%COMPOSE_FILE%" pull'
                
                // Starts all services in detached background mode (-d)
                bat 'docker compose -f "%COMPOSE_FILE%" up -d'
                
                // Lists all currently running containers and their exposed port mappings
                bat 'docker compose -f "%COMPOSE_FILE%" ps'
            }
        }
    }

    // ==========================================
    // POST-BUILD ACTIONS & LIFECYCLE HOOKS
    // ==========================================
    post {
        // Executes every single time, whether the build succeeded, failed, or was aborted
        always {
            echo '=== POST ACTION: Logging out of Docker Hub to clear session credentials ==='
            
            // Logs out of Docker to prevent cached auth tokens from remaining on the build server.
            // 'returnStatus: true' ensures the pipeline does not fail even if logout was not required.
            bat returnStatus: true, script: 'docker logout'
        }

        // Executes only when all stages complete successfully
        success {
            echo '=== STATUS: Pipeline executed successfully! All containers deployed. ==='
        }

        // Executes if any step throws an unhandled error or nonzero exit code
        failure {
            echo '=== STATUS: Pipeline failed! Review the console output logs above for details. ==='
        }
    }
}