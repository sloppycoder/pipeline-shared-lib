// vars/standardBuild.groovy

// stanard build pipeline for spring boot microserfvice running in 
// container platform

def call(boolean skipScan = true, boolean skipDeploy = false) {

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
                return skipScan || env.GIT_BRANCH.startsWith('release/') 
              }
            }
            steps {
                sh 'echo sonar scan'
            }
        }
        
        stage('push container image') {
            steps {
                sh 'echo push image'
                sh 'echo podman push .... can use multiple tags'
            }
        }

        stage('deploy and test') {
            when{
              expression { 
                return skipDeploy || env.GIT_BRANCH == 'develop' 
              }
            }
            steps {
                sh 'echo kubectl -k k8s/overlays/dev'
                sh 'echo run bunch of integration test'
                sh 'echo can also trigger a down stream job for testing'
            }
        }
       
    }
}


}
