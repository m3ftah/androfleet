#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()
print('Node')

NB_NODES = sys.argv[1]

print("NB_NODES = " + str(NB_NODES))

PATH = os.path.dirname(os.path.realpath(__file__))

print("Launching androfleet as node containers...")
i =int(NB_NODES)

weaveAddress = '192.168.49.' +str(i+1)

process = subprocess.Popen(['docker', 'run',
'--name','androfleet-node' + str(i),
'-d',
'--privileged',
'--net', os.environ['NETWORK'],
#'--ip', weaveAddress,
'--device', '/dev/kvm',
'm3ftah/androfleet-base', 'node', str(i), PORT], stdout=subprocess.PIPE).wait()

print('Node' + str(i))
