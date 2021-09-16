
 def credentialsId="8fda5689-e287-4fcc-8e67-f5a9f90f5cd0"
 def gitUrl="git@github.com:sydneyfullstack/roombooking.git"
 def dockerUser="hcoin"
 def imageName="${project_name}"
 def tag="latest"
 def docker_network="booking-network"
 def dockerCredential="795b8274-94c3-42e6-890f-d5f8e6c05d4d"


node {

    stage('pull from git') {
        echo 'pull from git.....'
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [[$class: 'PathRestriction', excludedRegions: '', includedRegions: 'roombooking-app/.*']], userRemoteConfigs: [[credentialsId: "${credentialsId}", url: "${gitUrl}"]]])
    }

    stage('install common module') {
        sh "mvn -f roombooking-common clean install"
    }

    stage('install microservice module') {
        sh "mvn -f ${project_name} clean package -Dmaven.test.skip=true"
    }

    stage('docker build image') {
        sh "docker build -f ${project_name}/Dockerfile --build-arg JAR_FILE=${project_name}/target/${project_name}-1.0-SNAPSHOT.jar -t ${dockerUser}/${imageName}:${tag} ."
    }

    stage('push image to docker hub') {
        withCredentials([usernamePassword(credentialsId: "${dockerCredential}", passwordVariable: 'password', usernameVariable: 'username')]) {
            sh "docker login -u ${username} -p ${password} "
            sh "docker push ${dockerUser}/${imageName}:${tag}"
            echo "Image pushed to docker hub success."
        }
    }

    stage('SSH execCommand') {
        sshPublisher(publishers: [sshPublisherDesc(configName: 'aws-prod', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/opt/jenkins_shell/deploy.sh ${dockerUser} ${project_name} ${tag} ${docker_network} ${port}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
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


