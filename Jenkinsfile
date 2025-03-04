pipeline {
    agent any
    
    environment {
        DOCKER_USERNAME = 'eladel686'
        BACKEND_REPO = 'https://github.com/Eladele/edu_click_backend.git'
        DOCKER_CREDENTIALS_ID = '3f680b4f-3020-49fe-be6d-d8f57d67676a'  // ID des credentials dans Jenkins
        BACK_IMAGE = 'edu-click-backend'
    }

    stages {
        stage('Checkout Backend') {
            steps {
                git branch: 'master', url: "${BACKEND_REPO}"
            }
        }
        
        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${DOCKER_USERNAME}/${BACK_IMAGE}:latest ."
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                        bat "docker push ${DOCKER_USERNAME}/${BACK_IMAGE}:latest"
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                bat '''
                    docker-compose down
                    docker-compose up -d
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
