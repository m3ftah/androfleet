#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Node')

NB_NODES = sys.argv[1]
APP = "NotNeeded"
PORT = sys.argv[3]

print("NB_NODES = " + str(NB_NODES))

PATH = os.path.dirname(os.path.realpath(__file__))


remoteAddress = ''
remotePort = '5555'

print("Launching androfleet as node containers...")
for i in range(int(NB_NODES)):
    time.sleep(2)

    weaveAddress = '192.168.49.' +str(i+1)

    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-emu' + str(i),
    '-d',
    #'--volumes-from=androfleet-data',
    '--privileged',
    '--net', 'my-net',
    '--ip', weaveAddress,
    '--device', '/dev/kvm',
    '-p', '50' + str(i + 1).zfill(3) + ':5900',
    #'-v',PATH + '/build:/build',
    'm3ftah/androfleet-emulator', 'node', APP, str(i), PORT], stdout=subprocess.PIPE).wait()

    weaveAddress = '192.168.50.' +str(i+1)

    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-node' + str(i),
    '-d',
    '--volumes-from=androfleet-data',
    '--privileged',
    '--net', 'my-net',
     '--ip', weaveAddress,
    'm3ftah/androfleet-base', 'node', APP, str(i), PORT], stdout=subprocess.PIPE).wait()

    print('Node' + str(i))

print(str(i + 1) + ' nodes launched.')
