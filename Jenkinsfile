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
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_TOKEN'
                    )
                ]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"

                        if ([string]::IsNullOrWhiteSpace($env:DH_USER)) {
                            throw "Docker Hub username is empty"
                        }

                        if ([string]::IsNullOrWhiteSpace($env:DH_TOKEN)) {
                            throw "Docker Hub token is empty"
                        }

                        $token = $env:DH_TOKEN.Trim()

                        $docker = (Get-Command docker.exe -ErrorAction Stop).Source

                        $psi = New-Object System.Diagnostics.ProcessStartInfo
                        $psi.FileName = $docker
                        $psi.Arguments = "login --username `"$env:DH_USER`" --password-stdin"
                        $psi.UseShellExecute = $false
                        $psi.RedirectStandardInput = $true
                        $psi.RedirectStandardOutput = $true
                        $psi.RedirectStandardError = $true
                        $psi.CreateNoWindow = $true

                        $process = New-Object System.Diagnostics.Process
                        $process.StartInfo = $psi

                        [void]$process.Start()

                        $stdoutTask = $process.StandardOutput.ReadToEndAsync()
                        $stderrTask = $process.StandardError.ReadToEndAsync()

                        $process.StandardInput.WriteLine($token)
                        $process.StandardInput.Close()

                        $process.WaitForExit()

                        $stdout = $stdoutTask.Result
                        $stderr = $stderrTask.Result

                        if ($stdout) {
                            Write-Host $stdout
                        }

                        if ($process.ExitCode -ne 0) {
                            Write-Host "Docker Hub login failed. Verify the Jenkins credential and access token."
                            if ($stderr) {
                                Write-Host $stderr
                            }
                            exit $process.ExitCode
                        }

                        if ($stderr) {
                            Write-Host $stderr
                        }

                        Write-Host "Docker Hub login successful."
                    '''

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
