pipeline {
    agent any
    
    environment {
        DOCKER_CREDENTIALS = credentials('b85dedd2-5df9-4384-9800-0b946e2ab996')
        BACKEND_REPO = 'https://github.com/Eladele/edu_click_backend.git'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: "${BACKEND_REPO}"
            }
        }
        
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t eladel686/edu-click-backend:latest .'
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                bat '''
                    echo %DOCKER_CREDENTIALS_PSW% | docker login -u %DOCKER_CREDENTIALS_USR% --password-stdin
                    docker push eladel686/edu-click-backend:latest
                '''
            }
        }
    }
    
    post {
        always {
            bat 'docker logout'
            cleanWs()
        }
    }
}