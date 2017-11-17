printf "[ ${WARN}Building core${NC} ]\n"
./build-core.py
printf "[ ${WARN}copying build${NC} ]\n"
site="$(docker inspect --format='{{.Node.Name}}' androfleet-master)";
docker cp androfleet-data/build $site/androfleet-master:/
#./restart.py
./retry.py 2


# for container in `docker ps -q`; do
#
#   # show the name of the container
#   name="$(docker inspect --format='{{.Name}}' $container)";
#   site="$(docker inspect --format='{{.Node.Name}}' $container)";
#   echo "$site$name"
#   # run the command (date in the case)
#   docker exec -it $site$name /build/restart.sh;
# done
