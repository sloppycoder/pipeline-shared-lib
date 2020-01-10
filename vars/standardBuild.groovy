// vars/standardBuild.groovy

// stanard build pipeline for spring boot microserfvice running in 
// container platform

def call(boolean doScan = false, boolean doDeploy = false) {

pipeline {

    agent { label 'linux' }

    stages {

        stage('build and unit test') {
            steps {
                sh 'env'
                sh 'echo mvn clean test jib:buildTar -P jib'
            }
        }

        stage('code quality and security scan') {
            when{
              expression { 
                return env.GIT_BRANCH.startsWith('release/') || doScan
              }
            }
            steps {
                sh 'echo sonar scan 2'
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
                sh 'echo push image with tag ' + env.GIT_BRANCH 
                sh 'echo push image with tag ' + env.GIT_COMMIT[-8..-1]
            }
        }

        stage('deploy and test') {
            when{
              expression { 
                return env.GIT_BRANCH == 'develop' || doDeploy
              }
            }
            steps {
                sh 'echo kubectl -k k8s/overlays/' + deployEnv
                sh 'echo run bunch of integration test'
                sh 'echo can also trigger a down stream job for testing'
            }
        }
       
    }
}


}


@groovy.transform.Field
def deployEnv = {
    if (env.GIT_BRANCH == 'develop') {
       return 'dev'
    } else if (env.GIT_BRANCH.startsWith('release/')) {
       return 'sit'
    } else {
       return 'any'
    }
}()
