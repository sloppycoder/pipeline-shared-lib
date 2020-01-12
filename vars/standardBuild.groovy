// vars/standardBuild.groovy

// stanard build pipeline for spring boot microserfvice running in 
// container platform

import Util

def call(boolean doScan = false, boolean doDeploy = false) {

pipeline {

    agent { label 'linux' }

    // make sure the toos are configured properly
    tools { 
        jdk 'openjdk-11' 
        maven 'maven-3.6.3' 
    }

    stages {

        stage('build and unit test') {
            steps {
                sh 'env'
                // use flag to disable transfer progress which creates tons of 
                // noise in Jenkins output
                sh 'mvn clean test jib:buildTar --no-transfer-progress -P jib'
            }
        }

        stage('code scan') {
            when{
              expression { 
                return env.GIT_BRANCH.startsWith('release/') || doScan
              }
            }
            steps {
                sh 'echo sonar scan'
                sh 'echo burpsuite scan'
            }
        }
        
        stage('push container image') {
            // we always tag the container image with short version of git hash
            // for image to source code tracibility 
            // and the branch name
            // later we'll just use a simple branch name to environment strategy 
            // to determine what images gets deploy to which environment
            // 
            // each environment will be defined as a kustomize overlay in source repo
            steps {
                sh 'echo push image with tag ' + Util.tagForBranch(env.GIT_BRANCH)
                sh 'echo push image with tag ' + env.GIT_COMMIT[-8..-1]
            }
        }

        stage('deploy') {
            when{
              expression { 
                return env.GIT_BRANCH == 'develop' || doDeploy
              }
            }
            steps {
                sh 'echo kubectl -k k8s/overlays/' + Util.envForBranch(env.GIT_BRANCH)
            }
        }

        stage('integration test') {
            when{
              expression { 
                return env.GIT_BRANCH == 'develop' || doDeploy
              }
            }
            steps {
                sh 'echo run bunch of integration tests'
                sh 'echo or trigger a down stream integration test job'
            }
        }
       
    }
} // of pipeline

}
