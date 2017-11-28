#!/bin/bash
source .env


echo "Running image in $NETWORK..."

docker run --name python-server -d -p 8080:8080 \
--net $NETWORK \
m3ftah/python-server

echo 'Mobiperf Python Server is run'
