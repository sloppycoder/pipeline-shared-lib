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
                branch 'release/*'
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

        stage('deploy') {
            when{
                branch 'develop'
            }
            steps {
                sh 'echo kubectl -k k8s/overlays/dev'
            }
        }
       
    }
}


}