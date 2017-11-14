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
docker build . -t m3ftah/androfleet-base && \
printf "[ ${OK}Image m3ftah/androfleet-base was built${NC} ]\n" ||\
exit 1

printf "[ ${WARN}Pushing m3ftah/androfleet-base image${NC} ]\n"
docker push m3ftah/androfleet-base && \
printf "[ ${OK}Image m3ftah/androfleet-base was pushed${NC} ]\n"||\
exit 1
cd ..

printf "[ ${WARN}Building core${NC} ]\n"
./build-core.py && \
printf "[ ${OK}Core was built${NC} ]\n" ||\
exit 1

cd androfleet-data
printf "[ ${WARN}Building m3ftah/androfleet-data${NC} ]\n"
docker build  . -t m3ftah/androfleet-data -q && \
printf "[ ${OK}Image m3ftah/androfleet-data was built${NC} ]\n" ||\
exit 1
cd ..

printf "[ ${WARN}Pushing m3ftah/androfleet-data image${NC} ]\n"
docker push m3ftah/androfleet-data && \
printf "[ ${OK}Image m3ftah/androfleet-data was pushed${NC} ]\n"||\
exit 1


printf "[ ${WARN}Changing environment to $CLUSTER-0${NC} ]\n"
eval $(docker-machine env --swarm $CLUSTER-0) && \
printf "[ ${OK}Environment changed${NC} ]\n"||\
exit 1



printf "[ ${WARN}Pulling m3ftah/androfleet-data image for cluster: $CLUSTER${NC} ]\n"
docker pull m3ftah/androfleet-data && \
printf "[ ${OK}Image m3ftah/androfleet-data was pulled${NC} ]\n"||\
exit 1


printf "[ ${WARN}Pulling m3ftah/androfleet-base image for cluster: $CLUSTER${NC} ]\n"
docker pull m3ftah/androfleet-base && \
printf "[ ${OK}Image m3ftah/androfleet-base was pulled${NC} ]\n"||\
exit 1
