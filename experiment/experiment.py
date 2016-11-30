#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Launching experiment')

NB_NODES = sys.argv[1]

subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call(['./clean.py'])

print("Launching database")
subprocess.call(['./db.py'])

print("Launching Master")
subprocess.call(['./master.py', NB_NODES])

time.sleep(5)

print("Launching Service Discovery")
subprocess.call(['./servicediscovery.py'])

print("Launching Service Discovery")
subprocess.call(['./node.py',NB_NODES])

print("androfleet-master log :")
subprocess.call(['docker','logs','-f','androfleet-master'])