#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser



NB_NODES = "2" if len(sys.argv) < 2 else sys.argv[1]
APP = 'fr.inria.rsommerard.fougereapp/.MainActivity'
PORT = '11131'
ADB_PATH = '/home/lakhdar/Android/Sdk/platform-tools/adb'
PYTHONS_PATH = '.'

print('Launching experiment with: ' + NB_NODES + ' nodes')


subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([PYTHONS_PATH + '/cleanAndrofleet.py'])

print("Launching Weave")
subprocess.call(['weave', 'launch','--ipalloc-range','192.168.48.0/23'])

print("Exposing Weave")
subprocess.call(['weave', 'expose'])

print("Exposing xhost")
subprocess.call(['xhost', '+'])

print("Launching adb")
subprocess.call([ADB_PATH, 'devices'])

print("Redirecting adb port to weave")
subprocess.Popen(['redir', '--cport', '5037', '--caddr', '127.0.0.1', '--lport', '5037', '--laddr', '192.168.48.1', '&'])

print("Launching Master")
subprocess.call([PYTHONS_PATH + '/master.py', NB_NODES])

time.sleep(5)
print("Launching Service Discovery")
subprocess.call([PYTHONS_PATH + '/servicediscovery.py'])


print("Launching Nodes")

subprocess.call([PYTHONS_PATH + '/node.py',NB_NODES,APP,PORT])
