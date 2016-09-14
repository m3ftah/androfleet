#!/usr/bin/python3

import subprocess
import os
import sys
import shutil
import time

print('SOCIAL')

# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value

# start social container
print("Launching androfleet as social container...")
process = subprocess.Popen(['docker', 'run', '--name', 'androfleet-social', '-d', '-e', 'WEAVE_CIDR=10.32.0.44/12', 'rsommerard/androfleet', 'social'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'a+') as f:
    f.write("Social=" + output)

time.sleep(3)
