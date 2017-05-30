#!/usr/bin/python3

import subprocess
import os
import sys
import time
from os.path import expanduser
home = expanduser("~")

print('Launching docker')

NB_NODES = sys.argv[1]

subprocess.call(['pwd'])
print("Cleaning...")
subprocess.call([home + '/ilab/androfleet/docker/cleanAndrofleet.py'])

print("Launching Weave")
subprocess.call(['weave', 'launch'])

print("Exposing Weave")
subprocess.call(['weave', 'expose'])

print("Launching adb")
subprocess.call(['adb', 'devices'])

print("Rdirecting adb port to weave")
subprocess.call(['redir', '--cport', '5037', '--caddr', '127.0.0.1', '--lport', '5037', '--laddr', '10.32.0.2', '&'])

print("Launching Master")
subprocess.call([home + '/ilab/androfleet/docker/master.py', NB_NODES])

time.sleep(5)
print("Launching Service Discovery")
subprocess.call([home + '/ilab/androfleet/docker/servicediscovery.py'])


print("Launching Nodes")

subprocess.call([home + '/ilab/androfleet/docker/node.py',NB_NODES])

print("androfleet-master log :")
subprocess.call(['docker','logs','-f','androfleet-master'])
