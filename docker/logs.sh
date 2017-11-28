#!/bin/bash
source .env
#sleep 4200
eval $(docker-machine env --swarm $SITE-0)
FOLDER=logs/$(date +%Y_%m_%d_%H_%M_%S)
 mkdir $FOLDER
 read -p "Please enter a comment with the log: "  comment
 echo "comment: $comment!"
 echo "$comment" > $FOLDER/comment.log

 #sleep 18000
 for (( i=0; i< $NODES; ++i));
 do
   site="$(docker inspect --format='{{.Node.Name}}' androfleet-node$i)";
   echo "Getting log for $site/androfleet-node$i"
   docker logs $site/androfleet-node$i > $FOLDER/androfleet-node$i.log  2>&1;

   site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu$i)";
   echo "Getting log for $site/androfleet-emu$i"
   docker logs $site/androfleet-emu$i > $FOLDER/androfleet-emu$i.log  2>&1;
 done


site="$(docker inspect --format='{{.Node.Name}}' androfleet-master)";
echo "Getting log for $site/androfleet-master"
docker logs $site/androfleet-master > $FOLDER/androfleet-master.log  2>&1;


site="$(docker inspect --format='{{.Node.Name}}' androfleet-servicediscovery)";
echo "Getting log for $site/androfleet-servicediscovery"
docker logs $site/androfleet-servicediscovery > $FOLDER/androfleet-servicediscovery.log  2>&1;

docker-machine ls > $FOLDER/docker-machine.log 2>&1

git show --oneline -s > $FOLDER/git.log 2>&1
cp $APK $FOLDER/debug.apk
