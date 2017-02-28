#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Launching experiment')

NB_NODES = sys.argv[1]

subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call(['/home/lakhdar/ilab/androfleet/experiment/clean.py'])

print("Launching database")
subprocess.call(['/home/lakhdar/ilab/androfleet/experiment/db.py'])

print("Launching Master")
subprocess.call(['/home/lakhdar/ilab/androfleet/experiment/master.py', NB_NODES])

time.sleep(5)

print("Launching Service Discovery")
subprocess.call(['/home/lakhdar/ilab/androfleet/experiment/servicediscovery.py'])

print("Launching Service Discovery")
subprocess.call(['/home/lakhdar/ilab/androfleet/experiment/node.py',NB_NODES])

print("androfleet-master log :")
subprocess.call(['docker','logs','-f','androfleet-master'])
