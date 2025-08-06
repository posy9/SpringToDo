pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_IMAGE = 'posy9/spring-to-do'
    }

    stages {
        stage('PR Check') {
             when {
                       changeRequest target: 'dev'
                   }
            steps {
                script {
                    bat 'mvn clean install'
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

                    bat 'mvn clean package'


                    docker.build("${DOCKER_IMAGE}:${env.BUILD_NUMBER}")

                    bat "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"

                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE}:${env.BUILD_NUMBER}").push()
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
