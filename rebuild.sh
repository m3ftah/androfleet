#!/bin/bash
echo "Cleaning Androfleet docker containers"
/home/lakhdar/ilab/androfleet/experiment/clean.py
echo "Copying Fougere..."
/home/lakhdar/ilab/androfleet/android/fougere.py
echo "Building Androfleet Docker container"
/home/lakhdar/ilab/androfleet/docker/androfleet/build.py
echo "Launching experiment with $1 nodes"
/home/lakhdar/ilab/androfleet/experiment/experiment.py $1

