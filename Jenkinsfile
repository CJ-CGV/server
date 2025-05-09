pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-northeast-2' // 서울
        AWS_ACCOUNT_ID = '911167907616'
        ECR_REPO_NAME = 'cgv-server'
        IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git credentialsId: 'git-creds-server',
                    url: 'https://github.com/CJ-CGV/server.git',
                    branch: 'main'
            }
        }

        stage('✅ application.properties 생성') {
              steps {
                  withCredentials([file(credentialsId: 'app-properties1', variable: 'APP_PROPERTIES')]) {
                    sh '''
                    echo "📁 ./src/main/resources 생성"
                    mkdir -p ./src/main/resources
                    cp "$APP_PROPERTIES" ./src/main/resources/application.properties
                    '''
                  }
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

        stage('Push Docker Image to ECR') {
            steps {
                sh '''
                    echo "🔐 ECR 로그인"
                    aws ecr get-login-password | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

                    echo "🐳 Docker 이미지 푸시 중..."
                    docker push $DOCKER_IMAGE
                '''
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
                        sed -i "s|image: .*|image: ${DOCKER_IMAGE}|" cgv-server/deployment.yaml
                        git add cgv-server/deployment.yaml
                        git commit -m "Update image tag to ${IMAGE_TAG}"
                        git push https://$GITOPS_TOKEN@github.com/yeonjeong2/gitops.git
                    '''
                }
            }
        }
    }
    post {
                always {
                  echo "🧹 파이프라인 실행 후 워크스페이스 정리"
                  cleanWs()
                }
              }
}
