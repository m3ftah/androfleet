source .env

eval $(docker-machine env --swarm $CLUSTER-0)
docker exec -it lille-0/androfleet-node0 adb devices
docker exec -it lille-0/androfleet-node0 adb install $APK
adb -s $ME:5555 -e shell am start -n $2
