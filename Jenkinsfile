pipeline {
    agent any
    
    environment {
        DOCKER_CREDENTIALS = credentials('7d1ad480-eebe-41b9-83a7-491290aa1274')
        BACKEND_REPO = 'https://github.com/abdallahilili/edut_click.git'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: "${BACKEND_REPO}"
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t eladel686/edu-click-backend:latest .'
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                sh '''
                    echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                    docker push eladel686/edu-click-backend:latest
                '''
            }
        }
    }
    
    post {
        always {
            sh 'docker logout'
            cleanWs()
        }
    }
}