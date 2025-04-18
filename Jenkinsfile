pipeline {
  agent any

  environment {
    IMAGE_NAME     = "duswjd/cgv-server"  // DockerHub 이미지 이름
    IMAGE_TAG      = ""  // 이후 단계에서 커밋 해시로 설정
    GITOPS_REPO    = "https://github.com/CJ-CGV/gitops.git"
    GITOPS_BRANCH  = "main"
    DEPLOYMENT_YAML_PATH = "/root/gitops/cgv-server/deployment.yaml"
  }

  stages {
    stage('Clone Source') {
      steps {
        git credentialsId: 'git-creds-server', url: 'https://github.com/CJ-CGV/server.git', branch: 'main'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
          env.IMAGE_TAG = IMAGE_TAG
        }
        sh """
          docker build -t $IMAGE_NAME:$IMAGE_TAG .
          docker tag $IMAGE_NAME:$IMAGE_TAG $IMAGE_NAME:latest
        """
      }
    }

    stage('Push Docker Image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh """
            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
            docker push $IMAGE_NAME:$IMAGE_TAG
            docker push $IMAGE_NAME:latest
          """
        }
      }
    }

    stage('Update GitOps Deployment') {
      steps {
        dir('gitops') {
          git credentialsId: 'git-creds', url: "$GITOPS_REPO", branch: "$GITOPS_BRANCH"

          sh """
            sed -i 's|image: .*|image: $IMAGE_NAME:$IMAGE_TAG|' $DEPLOYMENT_YAML_PATH

            git config user.name "jenkins-ci"
            git config user.email "jenkins@ci.com"

            git commit -am "Update image tag to $IMAGE_TAG"
            git push origin $GITOPS_BRANCH
          """
        }
      }
    }
  }
}