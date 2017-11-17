#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser
from env import env
env()


NB_NODES = "2" if len(sys.argv) < 2 else sys.argv[1]

ADB_PATH = '/home/lakhdar/Android/Sdk/platform-tools/adb'
PYTHONS_PATH = '.'

print('Relaunching experiment with: ' + NB_NODES + ' nodes')


subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([PYTHONS_PATH + '/cleanAndrofleet-base.py'])

#print("Rebuilding Androfleet...")
#subprocess.call([PYTHONS_PATH + '/rebuild.sh'])


#print("Launching Androfleet data container")
#subprocess.call([PYTHONS_PATH + '/data.py'])

print("Launching Master")
subprocess.call([PYTHONS_PATH + '/master.py', NB_NODES])
# time.sleep(5)

print("Launching Service Discovery")
subprocess.call([PYTHONS_PATH + '/servicediscovery.py'])


print("Relaunching Nodes")

subprocess.call([PYTHONS_PATH + '/node.py',NB_NODES])
