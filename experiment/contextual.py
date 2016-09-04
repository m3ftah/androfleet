#!/usr/bin/python3

import subprocess
import os
import sys
import shutil
import time

print('CONTEXTUAL')

# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value

# start contextual container
print("Launching androfleet as contextual container...")
process = subprocess.Popen(['docker', 'run', '-d', '-e', 'WEAVE_CIDR=10.32.0.45/12', 'rsommerard/androfleet', 'contextual'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'a+') as f:
    f.write("Contextual=" + output)

time.sleep(3)
