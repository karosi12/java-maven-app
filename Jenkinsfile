#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        DOCKER_REPO_SERVER = '260269607025.dkr.ecr.eu-central-1.amazonaws.com'
        DOCKER_REPO = "${DOCKER_REPO_SERVER}/java-maven-app"
        ANSIBLE_SERVER = "18.194.28.116"
    }
    stages {

        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('ansible') {
            steps {
                script {
                    gv.ansibleSetUp()
                }
            }
        }
        stage('execute ansible playbook') {
            steps {
                script {
                    gv.executeAnsible()
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
        stage("deploy") {
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_key_id')
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }   
}