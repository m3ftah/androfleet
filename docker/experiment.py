#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser
home = expanduser("~")



NB_NODES = "2" if len(sys.argv) < 2 else sys.argv[1]
print('Launching experiment with: ' + NB_NODES + ' nodes')


subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([home + '/ilab/androfleet/docker/cleanAndrofleet.py'])

print("Launching Weave")
subprocess.call(['weave', 'launch','--ipalloc-range','192.168.48.0/23'])

print("Exposing Weave")
subprocess.call(['weave', 'expose'])

print("Exposing xhost")
subprocess.call(['docker-machine','ssh','machine-test','xhost', '+'])

print("Launching adb")
subprocess.call(['docker-machine','ssh','machine-test','./platform-tools/adb', 'devices'])

print("Rdirecting adb port to weave")
subprocess.Popen(['docker-machine','ssh','machine-test','redir', '--cport', '5037', '--caddr', '127.0.0.1', '--lport', '5037', '--laddr', '192.168.48.1', '&'])

print("Launching Master")
subprocess.call([home + '/ilab/androfleet/docker/master.py', NB_NODES])

time.sleep(5)
print("Launching Service Discovery")
subprocess.call([home + '/ilab/androfleet/docker/servicediscovery.py'])


print("Launching Nodes")

subprocess.call([home + '/ilab/androfleet/docker/node.py',NB_NODES])

print("androfleet-master log :")
subprocess.call(['docker','logs','-f','androfleet-master'])
