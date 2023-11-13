def versionUpdate() {
    echo "Incrementing app version..."
} 

def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    /**  docker image push to docker hub
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t karosi12/demo-app:${IMAGE_NAME} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push karosi12/demo-app:${IMAGE_NAME}"
    }
    */
    withCredentials([usernamePassword(credentialsId: 'ecr-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
        sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
    }
} 

def deployApp() {
    echo "Deploying the docker image..."
    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
} 

return this