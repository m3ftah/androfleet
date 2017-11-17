#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()
#print()
print("Launching androfleet data container...")

weaveAddress = '192.168.48.4'
#docker run -v /build --name="androfleet-data" m3ftah/androfleet-data
process = subprocess.Popen(['docker', 'run',
'--name','androfleet-data',
'-d',
'--privileged',
'--net', os.environ['NETWORK'],
#'--ip', weaveAddress,
'--device', '/dev/kvm',
#'-p', '50' + str(i + 1).zfill(3) + ':5900',
'-v', '/build',
'm3ftah/androfleet-data'], stdout=subprocess.PIPE).wait()
print('androfleet data container started')
