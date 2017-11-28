#!/bin/bash
source .env

docker cp $APK androfleet-emu0:/app-debug.apk
docker cp $APK androfleet-emu1:/app-debug.apk
docker exec androfleet-emu0 adb -s emulator-5554  install -r -t /app-debug.apk > /dev/null &
docker exec androfleet-emu1 adb -s emulator-5554  install -r -t /app-debug.apk > /dev/null &
docker exec -it androfleet-emu0 adb -s emulator-5554 -e shell am start -n $PACKAGE > /dev/null
docker exec -it androfleet-emu1 adb -s emulator-5554 -e shell am start -n $PACKAGE
