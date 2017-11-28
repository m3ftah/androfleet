#!/bin/bash
source .env
#sleep 4200
eval $(docker-machine env --swarm $SITE-0)
# FOLDER=logs/$(date +%Y_%m_%d_%H_%M_%S)
#  mkdir $FOLDER
#  read -p "Please enter a comment with the log: "  comment
#  echo "comment: $comment!"
#  echo "$comment" > $FOLDER/comment.log
 for (( i=0; i< $NODES; ++i));
 do
   site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu$i)";
   count=$(docker logs $site/androfleet-emu$i 2>&1 |grep "Process done" |wc -l)
   echo "androfleet-node$i : $count"
 done
