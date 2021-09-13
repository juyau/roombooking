
 def credentialsId="8c7b4b8f-dcc0-4349-a624-b54314d350bc"
 def gitUrl="git@github.com:sydneyfullstack/roombooking.git"

node {

    stage('pull from git') {
        echo 'pull from git.....'
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [[$class: 'PathRestriction', excludedRegions: '', includedRegions: 'roombooking-app/.*']], userRemoteConfigs: [[credentialsId: "${credentialsId}", url: "${gitUrl}"]]])
    }

    stage('install common module') {
        sh "mvn -f roombooking-common clean install"
    }
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

//      post {
//         success {
//             emailext(
//                 subject: 'Build success notification',
//                 body: 'build notification body',
//                 to: 'dryleeks7@gmail.com'
//             )
//         }
//
//         failure {
//             emailext(
//                 subject: 'Build failed notification',
//                 body: 'build notification body',
//                 to: 'dryleeks7@gmail.com'
//             )
//         }
//     }


