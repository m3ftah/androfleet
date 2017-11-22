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


print("Building core...")
subprocess.call([PYTHONS_PATH + '/build-core.py'])

print("Rebuilding Androfleet...")
subprocess.call([PYTHONS_PATH + '/rebuild.sh'])


print("Creating network...")
subprocess.Popen(['docker','network','create',
'--driver','overlay',
'--subnet=192.168.0.0/16',
#'--subnet=192.168.48.0/22',
os.environ['NETWORK']
]).wait()


print("Launching Androfleet data container")
subprocess.call([PYTHONS_PATH + '/data.py'])

# print("Launching Androfleet dataEmulator container")
# subprocess.call([PYTHONS_PATH + '/dataEmulator.py'])

print("Launching Mobiperf container")
subprocess.call([PYTHONS_PATH + '/mobiperfServer.sh'])

print("Launching Master")
subprocess.call([PYTHONS_PATH + '/master.py', os.environ['NODES']])
# time.sleep(5)

print("Launching Service Discovery")
subprocess.call([PYTHONS_PATH + '/servicediscovery.py'])


print("Launching Emulators")
subprocess.call([PYTHONS_PATH + '/emulator.py',os.environ['NODES'],APP,PORT])

print("Launching Nodes")
subprocess.call([PYTHONS_PATH + '/node.py',os.environ['NODES']])

# time.sleep(10)
print("Running Mobiperf apk")

subprocess.call([PYTHONS_PATH + '/runApk.sh'])
