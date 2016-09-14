#!/usr/bin/python3

import subprocess
import os
import sys
import shutil
import time

print('UI')

# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value

# start ui container
print("Launching androfleet as ui container...")
process = subprocess.Popen(['docker', 'run', '--name', 'androfleet-ui', '-d', '-p', '3012:8080', 'rsommerard/androfleet', 'ui'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'a+') as f:
    f.write("UI=" + output)

time.sleep(3)
