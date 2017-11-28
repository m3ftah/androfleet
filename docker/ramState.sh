#!/bin/bash
source .env
#./gradlew assemble clean
#./gradlew assemble
eval $(docker-machine env --swarm $SITE-0)

for (( i=0; i< $MACHINES; ++i));
do
  echo "lille-$i : $(docker-machine ssh lille-$i free -h -t |grep "Mem:" |awk '{print $4}')"
done
