#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()
print('Emulator')

NB_NODES = sys.argv[1]
PORT = '11131'#Fougere
#PORT = '8888'#anuj.wifidirect
#PORT = '1080'#com.colorcloud.wifichat

print("NB_EMULATORS = " + str(NB_NODES))

PATH = os.path.dirname(os.path.realpath(__file__))


print("Launching androfleet as Emulator containers...")
for i in range(int(NB_NODES)):
    time.sleep(2)

    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-emu' + str(i),
    '-d',
    '--privileged',
    '--net', os.environ['NETWORK'],
    #'--device', '/dev/kvm',
    'm3ftah/androfleet-emulator', 'node', str(i), PORT], stdout=subprocess.PIPE).wait()

    print('Emulator' + str(i))

print(str(i + 1) + ' Emulators launched.')
