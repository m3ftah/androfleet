#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser
home = expanduser("~")

print('Launching experiment')

NB_NODES = sys.argv[1]

subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([home + '/ilab/androfleet/experiment/clean.py'])

print("Launching Master")
subprocess.call([home + '/ilab/androfleet/experiment/master.py', NB_NODES])

time.sleep(5)
print("Launching Service Discovery")
subprocess.call([home + '/ilab/androfleet/experiment/servicediscovery.py'])


print("Launching Nodes")

subprocess.call([home + '/ilab/androfleet/experiment/node.py',NB_NODES])

print("androfleet-master log :")
subprocess.call(['docker','logs','-f','androfleet-master'])
