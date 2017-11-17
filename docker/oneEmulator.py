#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()
print('Emulator')

NODE = sys.argv[1]
PORT = '11131'#Fougere
#PORT = '8888'#anuj.wifidirect
#PORT = '1080'#com.colorcloud.wifichat
i = int(NODE)
print("Emulator = " + str(NODE))

PATH = os.path.dirname(os.path.realpath(__file__))


print("Launching androfleet as Emulator containers...")

weaveAddress = '192.168.49.' +str(i+1)

process = subprocess.Popen(['docker', 'run',
'--name','androfleet-emu' + str(i),
'-d',
#'--volumes-from=androfleet-data',
'--privileged',
'--net', os.environ['NETWORK'],
# '--ip', weaveAddress,
'--device', '/dev/kvm',
#'-p', '50' + str(i + 1).zfill(3) + ':5900',
#'-v',PATH + '/build:/build',
'm3ftah/androfleet-emulator', 'node', str(i), PORT], stdout=subprocess.PIPE).wait()
print('Emulator' + str(i))
