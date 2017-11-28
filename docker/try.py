#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
from os.path import dirname, abspath

local_dir = abspath(dirname(__file__))
env()

APP = 'NotNEEDED'
PORT = '11131'#Fougere
#network='my-net1'
#PORT = '8888'#anuj.wifidirect
#PORT = '1080'#com.colorcloud.wifichat
ADB_PATH = '/home/lakhdar/Android/Sdk/platform-tools/adb'
PYTHONS_PATH = '.'

print('Launching experiment with: ' + os.environ['NODES'] + ' nodes')


subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([PYTHONS_PATH + '/cleanAndrofleet.py'])


# print("Building core...")
# subprocess.call([PYTHONS_PATH + '/build-core.py'])
#
print("Rebuilding Androfleet...")
subprocess.call([PYTHONS_PATH + '/rebuild.sh'])

#docker network create --driver overlay --subnet=10.0.9.0/24 my-net
print("Creating network...")
subprocess.Popen(['docker','network','create',
'--driver','overlay',
'--subnet=192.168.0.0/16',
os.environ['NETWORK']
]).wait()


print("Launching Mobiperf container")
subprocess.call([PYTHONS_PATH + '/mobiperfServer.sh'])

print("Launching Master")
subprocess.call([PYTHONS_PATH + '/master.py', os.environ['NODES']])

print("Launching Service Discovery")
subprocess.call([PYTHONS_PATH + '/servicediscovery.py'])


print("Launching Emulators")
subprocess.call([PYTHONS_PATH + '/emulator.py',os.environ['NODES'],APP,PORT])

print("Launching Nodes")
subprocess.call([PYTHONS_PATH + '/node.py',os.environ['NODES']])

# time.sleep(10)
print("Running Mobiperf apk")

subprocess.call([PYTHONS_PATH + '/runApk.sh'])
