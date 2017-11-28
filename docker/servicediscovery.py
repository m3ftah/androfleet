#!/usr/bin/python3

import subprocess
import os
import time
from env import env
env()
print('ServiceDiscovery')


PATH = os.path.dirname(os.path.realpath(__file__))

# set weave env before launching containers
#print("Setting weave env...")
# process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
# output = str(process.communicate()[0], 'UTF-8')
# env = output.split(' ')[1:]
# for e in env:
#     name, value = e.split('=')
#     if not value.isspace():
#         os.environ[name] = value

# start servicediscovery container
print("Launching androfleet as servicediscovery container...")

process = subprocess.Popen(['docker', 'run', '--name', 'androfleet-servicediscovery',
'-d',
'--net', os.environ['NETWORK'],
'm3ftah/androfleet-base',
'servicediscovery'], stdout=subprocess.PIPE)

output = str(process.communicate()[0], 'UTF-8')

time.sleep(3)
