#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()
#print()
print("Launching androfleet dataEmulator container...")

#docker run -v /build --name="androfleet-data" m3ftah/androfleet-data
process = subprocess.Popen(['docker', 'run',
'--name','androfleet-dataEmulator',
'-d',
'--privileged',
'--net', os.environ['NETWORK'],
#'--ip', weaveAddress,
'--device', '/dev/kvm',
#'-p', '50' + str(i + 1).zfill(3) + ':5900',
'-v', '/build',
'-v', '/opt',
'm3ftah/androfleet-emulator', 'node', "55", "5555"], stdout=subprocess.PIPE).wait()
print('androfleet dataEmulator container started')
