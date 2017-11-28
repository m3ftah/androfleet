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
for i in range(1,int(NB_NODES)):
    #time.sleep(2)
    po = 0
    if i >= int(os.environ['MACHINES']): po = i
    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-node' + str(i),
    '-d',
    '--privileged',
    '-p', str(po + 9011) + ':9011',
    '--net', os.environ['NETWORK'],
    'm3ftah/androfleet-base', 'node', str(i)], stdout=subprocess.PIPE).wait()

    print('Node' + str(i))
#
# process = subprocess.Popen(['docker', 'run',
# '--name','androfleet-node0',
# '-d',
# '--volumes-from=androfleet-data',
# '--privileged',
# '--net', os.environ['NETWORK'],
# #'--ip', weaveAddress,
# 'm3ftah/androfleet-base', 'node', str(0)], stdout=subprocess.PIPE).wait()

print(str(i + 1) + ' nodes launched.')
