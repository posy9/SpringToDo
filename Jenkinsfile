pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_IMAGE = 'posy9/Spring-to-do'
    }

    stages {
        stage('PR Check') {
            when {
                changeRequest target: 'dev'
            }
            steps {
                script {
                    echo "Checking PR to dev branch..."
                    sh 'mvn clean install'

                }
            }
        }

        stage('Build and Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "Building and deploying Docker image..."


                    sh 'mvn clean package'


                    docker.build("${DOCKER_IMAGE}:${env.BUILD_NUMBER}")

                    sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"

                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE}:${env.BUILD_NUMBER}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
