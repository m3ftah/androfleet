#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Master')

NB_NODES = sys.argv[1]
PATH = os.path.dirname(os.path.realpath(__file__))

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

# start master container
print("Launching androfleet as master container...")
subprocess.call(['docker', 'run', '--name', 'androfleet-master',
'-v',PATH + '/build:/build',
 '-d', '-e', 'WEAVE_CIDR=10.32.0.42/12', 'androfleet', 'master', NB_NODES])
