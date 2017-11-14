#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Node')

NB_NODES = sys.argv[1]
APP = "NotNeeded"
# PORT = sys.argv[3]
PORT = '11131'
print("NB_NODES = " + str(NB_NODES))

PATH = os.path.dirname(os.path.realpath(__file__))


remoteAddress = ''
remotePort = '5555'

print("Launching androfleet as node containers...")
i =int(NB_NODES)

weaveAddress = '192.168.49.' +str(i+1)

process = subprocess.Popen(['docker', 'run',
'--name','androfleet-node' + str(i),
'-d',
'--privileged',
'--net', 'my-net',
 '--ip', weaveAddress,
'--device', '/dev/kvm',
'-p', '50' + str(i + 1).zfill(3) + ':5900',
#'-v',PATH + '/build:/build',
'm3ftah/androfleet', 'node', APP, str(i), PORT], stdout=subprocess.PIPE).wait()
print('Node' + str(i))
#process = subprocess.Popen(['docker', 'run', --name', 'androfleet-node' + str(i), '-d','--log-driver=gelf','--log-opt' ,'gelf-address=udp://172.17.0.3:12201','--log-opt','tag="node"' ,'--privileged', 'rsommerard/androfleet', 'node', APP], stdout=subprocess.PIPE)

print(str(i) + ' node launched.')
