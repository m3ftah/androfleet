#!/bin/bash
source .env
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

./docker-g5k create-cluster \
--g5k-username "$USERNAME" \
--g5k-password "$PASSWORD" \
--g5k-reserve-nodes "$CLUSTER:$MACHINES" \
--engine-opt "$CLUSTER-{0..$(($MACHINES-1))}:data-root=/tmp/docker" \
--g5k-walltime "$HOURS:00:00" \
--swarm-standalone-enable \
--swarm-master "$CLUSTER-0" \
--g5k-resource-properties "cluster = 'chifflet'" \
--g5k-image "ubuntu16.04-x64-min@gfieni" \
--engine-opt "$CLUSTER-{0..$(($MACHINES-1))}:storage-driver=overlay2"

printf "${GREEN}Node reservation succeeded${NC}\n"

# printf "[ ${WARN}Rebuilding Androfleet${NC} ]\n"
# ./rebuild.sh && \
# printf "[ ${OK}Androfleet is ready${NC} ]\n" ||\
# exit 1

# printf "[ ${WARN}Changing environment to $CLUSTER-0${NC} ]\n"
# eval $(docker-machine env --swarm $CLUSTER-0) && \
# printf "[ ${OK}Environment changed${NC} ]\n"||
# exit 1

# printf "[ ${WARN}Running $NODES nodes${NC} ]\n"
# ./try.py $NODES && \
# printf "[ ${OK}Androfleet is run with $NODES nodes${NC} ]\n" ||\
# exit 1
