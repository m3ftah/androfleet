#!/bin/bash
source .env
#./gradlew assemble clean
#./gradlew assemble
eval $(docker-machine env --swarm $SITE-0)



echo "Waiting for adb to connect to device..."


for (( i=0; i< $NODES; ++i));
do
  site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu$i)";
  status="$(docker inspect --format='{{.State.Status}}' androfleet-emu$i)";
  if [ "$status" != "created" ]; then
    CHECKED=0
    until [[ $CHECKED =~ 1 ]]; do
      echo "Checking if the emulator started?..."
      if docker logs $site/androfleet-emu$i 2>&1 | grep "Ping" ; then
        CHECKED=1
      else
        sleep 20
      fi
    done
    echo "Installing apk on $site/androfleet-emu$i"
    docker cp $APK $site/androfleet-emu$i:/app-debug.apk
    docker exec $site/androfleet-emu$i adb -s emulator-5554  install -r -t /app-debug.apk > /dev/null &
  fi
done
wait
#
# sleep 30
./oneNode.py 0

MasterSite="$(docker inspect --format='{{.Node.Name}}' androfleet-master)";
echo "wating for $MasterSite/androfleet-master"
CHECKED=0
until [[ $CHECKED =~ 1 ]]; do
  echo "checking master node for Starting process..."
  if docker logs $MasterSite/androfleet-master 2>&1 | grep "Starting process" ; then
    CHECKED=1
  fi
  sleep 5
done

for (( i=0; i< $NODES; ++i));
do
  site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu$i)"
  echo "Running apk on $site/androfleet-emu$i"
  status="$(docker inspect --format='{{.State.Status}}' androfleet-emu$i)"
  if [ "$status" != "created" ]; then
    docker exec -it $site/androfleet-emu$i adb -s emulator-5554 -e shell am start -n $PACKAGE > /dev/null
  fi
done

site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu0)";
docker logs -f $site/androfleet-emu0 |less
