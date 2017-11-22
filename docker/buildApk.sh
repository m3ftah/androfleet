#!/bin/bash
source .env
RETURN=$(pwd)
cd $APK_PROJECT
./gradlew build -x lint  clean #--stacktrace
./gradlew build -x lint #  --stacktrace
cd $RETRUN
