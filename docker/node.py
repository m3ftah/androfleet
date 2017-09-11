#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Node')

NB_NODES = sys.argv[1]
APP = sys.argv[2]
PORT = sys.argv[3]

print("NB_NODES = " + str(NB_NODES))

PATH = os.path.dirname(os.path.realpath(__file__))


remoteAddress = ''
remotePort = '5555'

print("Remote Address is: " + remoteAddress)


print("Launching androfleet as node containers...")
for i in range(int(NB_NODES)):

    weaveAddress = '192.168.49.' +str(i+1)

    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-node' + str(i),
    '-d',
    '--privileged',
    '--net', 'my-net',
     '--ip', weaveAddress,
    '--device', '/dev/kvm',
    '-p', '500' + str(i + 1) + ':5900',
    #'-v',PATH + '/build:/build',
    'androfleet', 'node', APP, str(i), PORT], stdout=subprocess.PIPE).wait()
    print('Node' + str(i))
    #process = subprocess.Popen(['docker', 'run', --name', 'androfleet-node' + str(i), '-d','--log-driver=gelf','--log-opt' ,'gelf-address=udp://172.17.0.3:12201','--log-opt','tag="node"' ,'--privileged', 'rsommerard/androfleet', 'node', APP], stdout=subprocess.PIPE)

print(str(i + 1) + ' nodes launched.')
