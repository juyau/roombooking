pipeline {
    agent any
    stages {
        stage('check env') {
            steps {
                sh '''export MAVEN_HOME=/opt/maven
                export PATH=$PATH:$MAVEN_HOME/bin'''

                sh '''java -version
                git --version
                mvn -version'''
            }
        }

//         stage('pull from git') {
//             steps {
//                 echo 'pull from git.....'
//                 checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: '6aceade2-91c3-40a8-8c7e-b00c75acb50d', url: 'https://github.com/sydneyfullstack/jenkinsdemo.git']]])
//             }
//         }
//
//         stage('maven build') {
//             steps {
//                  echo 'maven build.....'
//                  sh 'mvn clean package -Dmaven.test.skip=true'
//             }
//         }
//
//         stage('check container and delete') {
//             steps {
//                  sh '''echo \'check if container exists\'
//                  containerid=`docker ps -a | grep jenkinsdemo | awk \'{print $1}\'`
//                  if [ "$containerid" != "" ];then
//                          echo \'container exists, stop it\'
//                          docker stop $containerid
//                          echo \'delete container\'
//                          docker rm $containerid
//                  fi'''
//             }
//         }

//         stage('check image and delete') {
//             steps {
//                 sh '''echo \'check if image exists\'
//                 imageid=`docker images | grep jenkinsdemo | awk \'{print $3}\'`
//                 if [ "$imageid" != "" ];then
//                         echo \'delete image\'
//                         docker rmi -f $imageid
//                 fi'''
//             }
//         }
//
//         stage('docker build') {
//             steps {
//                  echo 'docker build'
//                  sh '''docker build --build-arg JAR_FILE=target/jenkinsdemo.jar -t hcoin/jenkinsdemo:1.0 .'''
//
//             }
//         }
//
//         stage('docker run') {
//             steps {
//                  echo 'docker start to run.....'
//                  sh 'docker run -itd --name jenkinsdemo -p 7777:8888 hcoin/jenkinsdemo:1.0'
//             }
//         }

    }

     post {
        success {
            emailext(
                subject: 'Build success notification',
                body: 'build notification body',
                to: 'dryleeks7@gmail.com'
            )
        }

        failure {
            emailext(
                subject: 'Build failed notification',
                body: 'build notification body',
                to: 'dryleeks7@gmail.com'
            )
        }
    }

}
