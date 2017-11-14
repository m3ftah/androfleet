#!/bin/bash
source .env
RED='\033[0;31m'
WARN='\033[93m'
OK='\033[92m'
NC='\033[0m' # No Color

eval "$(docker-machine env -u)"

# printf "[ ${RED}W'ont reBuilding m3ftah/androfleet-base${NC} ]\n"
cd ./androfleet-base
printf "[ ${WARN}Building m3ftah/androfleet-base${NC} ]\n"
docker build . -t m3ftah/androfleet-base
printf "[ ${WARN}Pushing m3ftah/androfleet-base image${NC} ]\n"
docker push m3ftah/androfleet-base
cd ..

printf "[ ${WARN}Building core${NC} ]\n"
./build-core.py

cd androfleet-data
printf "[ ${WARN}Building m3ftah/androfleet-data${NC} ]\n"
docker build  . -t m3ftah/androfleet-data -q
printf "[ ${WARN}Pushing m3ftah/androfleet-data image${NC} ]\n"
docker push m3ftah/androfleet-data
cd ..

cd androfleet-emulator
printf "[ ${WARN}Building m3ftah/androfleet-emulator${NC} ]\n"
docker build  . -t m3ftah/androfleet-emulator -q
printf "[ ${WARN}Pushing m3ftah/androfleet-emulator image${NC} ]\n"
docker push m3ftah/androfleet-emulator
cd ..


printf "[ ${WARN}Changing environment to $CLUSTER-0${NC} ]\n"
eval $(docker-machine env --swarm $CLUSTER-0) && \
printf "[ ${OK}Environment changed${NC} ]\n"||\
exit 1



printf "[ ${WARN}Pulling m3ftah/androfleet-data image for cluster: $CLUSTER${NC} ]\n"
docker pull m3ftah/androfleet-data

printf "[ ${WARN}Pulling m3ftah/androfleet-base image for cluster: $CLUSTER${NC} ]\n"
docker pull m3ftah/androfleet-base

printf "[ ${WARN}Pulling m3ftah/androfleet-emulator image for cluster: $CLUSTER${NC} ]\n"
docker pull m3ftah/androfleet-emulator
