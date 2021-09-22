
 def credentialsId="8fda5689-e287-4fcc-8e67-f5a9f90f5cd0"
 def gitUrl="https://github.com/sydneyfullstack/roombooking.git"
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

    stage('delete old  image') {
        sh '''imageId=$(docker images | grep "$project_name" | awk \'{print $3}\')
            if [ "$imageId" != "" ]
            then
            echo "image exists, deleting..."
            docker rmi -f "$imageId"
            echo "image deleted success."
            fi'''
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


