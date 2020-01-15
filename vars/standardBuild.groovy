// vars/standardBuild.groovy

// stanard build pipeline for spring boot microserfvice running in 
// container platform

import Util

def call(boolean doDeploy = false) {

pipeline {

    agent { label 'linux' }

    // make sure the toos are configured properly
    tools { 
        jdk 'openjdk-11' 
        maven 'maven-3.6.3' 
    }

    environment {
        DEPLOY_ENV = Util.envForBranch(env.GIT_BRANCH)
    }

    stages {

        stage('build and unit test') {
            steps {
                sh 'env'
                // use flag to disable transfer progress which creates tons of 
                // noise in Jenkins output
                sh 'mvn test'

            }
        }

        stage('push image') {
            when{
                expression {
                    BRANCH_NAME ==~ /release\/feature\/.+|develop/
                }
            }
            steps {
                // this step will build image and push to image registry
                sh 'mvn clean compile jib:build --no-transfer-progress -DskipTests -D-Dpush.image.tag=' + DEPLOY_ENV + ' -P jib'

            }
        }

        stage('code scan') {
            // do some scanning when code merge into develop branch
            // we want to scan early so that developers can fix them early
            // if performance is a concern we can make the scan faster by 
            // using less rigorous profiles.
            // we can later add one more rigorous scan in release branch
            when{
                branch  'release/*'
            }
            steps {
                sh 'echo quick sonar scan'
                sh 'echo quick burpsuite scan'
            }
        }
        
        stage('deploy') {
            when{
              expression { 
                GIT_BRANCH == 'develop' || doDeploy
              }
            }
            steps {
                sh 'kustomize build ./k8s/overlays/' + DEPLOY_ENV + ' | kubectl apply -f -'
            }
        }

        stage('integration test') {
            when{
              expression { 
                GIT_BRANCH == 'develop' || doDeploy
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
