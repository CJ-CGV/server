pipeline {
    agent any

    environment {
        IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_IMAGE = "duswjd/cgv-server:${IMAGE_TAG}"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git credentialsId: 'git-creds-server',
                    url: 'https://github.com/CJ-CGV/server.git',
                    branch: 'main'
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                chmod +x gradlew
                ./gradlew clean build -x test --no-daemon
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push $DOCKER_IMAGE
                    '''
                }
            }
        }

        stage('Update GitOps Repository') {
            steps {
                withCredentials([string(credentialsId: 'gitops', variable: 'GITOPS_TOKEN')]) {
                    sh '''
                        git config --global user.email "jenkins@ci.com"
                        git config --global user.name "jenkins"

                        rm -rf gitops
                        git clone https://$GITOPS_TOKEN@github.com/yeonjeong2/gitops.git
                        cd gitops
                        sed -i "s|image: duswjd/cgv-server:.*|image: duswjd/cgv-server:${IMAGE_TAG}|" cgv-server/deployment.yaml
                        git add cgv/deployment.yaml
                        git commit -m "Update image tag to ${IMAGE_TAG}"
                        git push https://$GITOPS_TOKEN@github.com/yeonjeong2/gitops.git
                    '''
                }
            }
        }
    }
}
