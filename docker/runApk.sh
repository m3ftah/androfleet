source .env
#./gradlew assemble clean
#./gradlew assemble
eval $(docker-machine env --swarm $CLUSTER-0)

for (( i=0; i< $NODES; ++i));
do
  site="$(docker inspect --format='{{.Node.Name}}' androfleet-node$i)";
  echo "Installing apk on $site/androfleet-node$i"
  docker cp $APK $site/androfleet-node$i:/app-debug.apk;

  docker exec -it $site/androfleet-node$i adb -s emulator-5554  install -r -t /app-debug.apk;
done

for (( i=0; i< $NODES; ++i));
do
  site="$(docker inspect --format='{{.Node.Name}}' androfleet-node$i)";
  echo "Running apk on $site/androfleet-node$i"
  docker exec -it $site/androfleet-node$i adb -s emulator-5554 -e shell am start -n $PACKAGE;
done
