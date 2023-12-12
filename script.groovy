def versionUpdate() {
    echo "Incrementing app version..."
} 

def ansibleSetUp() {
    echo "Copying all necessary files to ansible control node"
    sshagent(['ansible-server-key']) {
        sh "scp -o StrictHostKeyChecking=no ansible/* ubuntu@18.194.28.116:/home/ubuntu"

        withCredentials([sshUserPrivateKey(credentialsId: 'ec2-server-key', keyFileVariable: 'keyfile', usernameVariable: 'user')]) {
            sh "scp ${keyfile} ubuntu@18.194.28.116:/home/ubuntu/ssh-key.pem"
        }
    }
}

def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
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