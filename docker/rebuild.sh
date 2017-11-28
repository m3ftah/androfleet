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
docker build . -t m3ftah/androfleet-base -q
printf "[ ${WARN}Pushing m3ftah/androfleet-base image${NC} ]\n"
docker push m3ftah/androfleet-base
cd ..

printf "[ ${WARN}Building core${NC} ]\n"
./build-core.py


cd androfleet-emulator
printf "[ ${WARN}Building m3ftah/androfleet-emulator${NC} ]\n"
docker build  . -t m3ftah/androfleet-emulator -q
printf "[ ${WARN}Pushing m3ftah/androfleet-emulator image${NC} ]\n"
docker push m3ftah/androfleet-emulator
cd ..

cd mobiperf-server
printf "[ ${WARN}Building m3ftah/python-server${NC} ]\n"
docker build  . -t m3ftah/python-server -q
printf "[ ${WARN}Pushing m3ftah/python-server image${NC} ]\n"
docker push m3ftah/python-server
cd ..

printf "[ ${WARN}Changing environment to $SITE-0${NC} ]\n"
eval $(docker-machine env --swarm $SITE-0) && \
printf "[ ${OK}Environment changed${NC} ]\n"||\
exit 1



printf "[ ${WARN}Pulling m3ftah/androfleet-data image for SITE: $SITE${NC} ]\n"
docker pull m3ftah/androfleet-data

printf "[ ${WARN}Pulling m3ftah/androfleet-base image for SITE: $SITE${NC} ]\n"
docker pull m3ftah/androfleet-base

printf "[ ${WARN}Pulling m3ftah/androfleet-emulator image for SITE: $SITE${NC} ]\n"
docker pull m3ftah/androfleet-emulator

printf "[ ${WARN}Pulling m3ftah/androfleet-emulator image for SITE: $SITE${NC} ]\n"
docker pull m3ftah/python-server
