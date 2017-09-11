#!/usr/bin/python3

import subprocess
import os
import sys
import time
import os

if len(sys.argv) > 1:
    nodeMaster = sys.argv[1]
else:
    nodeMaster = 'luxembourg-0'

PYTHONS_PATH = '.'

#os.chdir('./docker')

print("get list of nodes...")
process = subprocess.Popen(['docker-machine', 'ls','--filter','swarm=' + nodeMaster,'--format', '{{.Name}}' ], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
lines = output.split("\n")[:-1]
print(lines)
for node in lines:
    print("node: " + node)
    process = subprocess.Popen(['docker-machine', 'env',node], stdout=subprocess.PIPE)
    output = str(process.communicate()[0], 'UTF-8')
    envlines = output.split('\n')[:-3]
    for e in envlines:
        export,cont = e.split(' ')
        name, value = cont.split('=')
        os.environ[name] = value[1:-1]
    process = subprocess.Popen(['docker', 'ps'], stdout=subprocess.PIPE)
    output = str(process.communicate()[0], 'UTF-8')

    subprocess.call(['pwd'])
    print("Cleaning...")
    subprocess.call([PYTHONS_PATH + '/cleanAndrofleet.py'])


    print("Building core...")
    subprocess.call([PYTHONS_PATH + '/build-core.py'])

    print("Building image...")
    subprocess.Popen([PYTHONS_PATH + '/build-image.py'], shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)
