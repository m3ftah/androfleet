#!/bin/bash
source .env


echo 'Mobiperf Python Server'

cd mobiperf-server



echo 'Building image'

docker build -t python-server .


echo "Running image in $NETWORK..."

docker run --name python-server -d -p 8080:8080 --net $NETWORK python-server

echo 'Mobiperf Python Server is run'

cd ..
