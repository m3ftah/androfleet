docker-machine scp remote.sh machine-test:~
docker-machine ssh machine-test chmod +x ./remote.sh
docker-machine ssh machine-test ./remote.sh
