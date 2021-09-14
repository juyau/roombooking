#!/bin/sh
#receive pass in params
docker_user=$1
project_name=$2
tag=$3
network=$4
port=$5

echo "check if container exists"

#check if container running and stop then delete
containerid=`docker ps -a | grep $project_name:$tag | awk '{print $1}'`
if [ $containerid != "" ]
then
echo "container exists, stopping..."
docker stop $containerid
echo "delete container..."
docker rm $containerid
echo "container removed success."
fi

#check if image exists and delete
imageid=`docker images | grep $project_name | awk '{print $3}'`
if [ $imageid != "" ]

echo "image exists, deleting..."
docker rmi -f $imageid
echo "image deleted success."
fi

#download image
docker pull $docker_user/$project_name:$tag

#run docker container
if [ $project_name == "gateway" ]
then
docker run --name $project_name --network $network -d -p $port:$port $docker_user/$project_name:$tag
else
docker run --name $project_name --network $network -d $docker_user/$project_name:$tag
fi

echo "container running on network - '$network'"
