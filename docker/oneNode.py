#!/usr/bin/python3

import subprocess
import os
import sys
import time
from env import env
env()

i = sys.argv[1]

print('Running androfleet-node' + str(i))

process = subprocess.Popen(['docker', 'run',
'--name','androfleet-node' + str(i),
'-d',
'--volumes-from=androfleet-data',
'--privileged',
'--net', os.environ['NETWORK'],
'm3ftah/androfleet-base', 'node', str(i)], stdout=subprocess.PIPE).wait()

print('androfleet-node' + str(i) + ' started')
