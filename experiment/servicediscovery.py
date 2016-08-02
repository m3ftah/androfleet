#!/usr/bin/python3

import subprocess
import os
import time

print('ServiceDiscovery')

# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value

# start servicediscovery container
print("Launching androfleet as servicediscovery container...")
process = subprocess.Popen(['docker', 'run', '-d', '-e', 'WEAVE_CIDR=10.32.0.43/12', 'rsommerard/androfleet', 'servicediscovery'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'a+') as f:
    f.write("ServiceDiscovery=" + output)

time.sleep(3)
