#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser



NB_NODES = "2" if len(sys.argv) < 2 else sys.argv[1]
APP = 'NotNEEDED'
PORT = '11131'#Fougere
#PORT = '8888'#anuj.wifidirect
#PORT = '1080'#com.colorcloud.wifichat
ADB_PATH = '/home/lakhdar/Android/Sdk/platform-tools/adb'
PYTHONS_PATH = '.'

print('Launching experiment with: ' + NB_NODES + ' nodes')


subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([PYTHONS_PATH + '/cleanAndrofleet.py'])


#print("Building core...")
#subprocess.call([PYTHONS_PATH + '/build-core.py'])

print("Rebuilding Androfleet...")
subprocess.call([PYTHONS_PATH + '/rebuild.sh'])


print("Creating network...")
subprocess.Popen(['docker','network','create','--driver','overlay', '--subnet=192.168.48.0/23','my-net']).wait()

print("Launching Master")
subprocess.call([PYTHONS_PATH + '/master.py', NB_NODES])

time.sleep(5)
print("Launching Service Discovery")
subprocess.call([PYTHONS_PATH + '/servicediscovery.py'])


print("Launching Nodes")

subprocess.call([PYTHONS_PATH + '/node.py',NB_NODES,APP,PORT])
