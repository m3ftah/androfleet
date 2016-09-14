#!/usr/bin/python3

import subprocess
import os
import time

print('DB')

# launch weave
print("Launching weave...")
subprocess.call(['weave', 'launch'])

# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value

# start mongo container
print("Launching mongo container...")
process = subprocess.Popen(['docker', 'run', '--name', 'androfleet-db', '-d', '-e', 'WEAVE_CIDR=10.32.0.41/12', 'mongo', '--rest'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

time.sleep(3)
