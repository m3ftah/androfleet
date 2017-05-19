#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Node')

NB_NODES = sys.argv[1]

print("NB_NODES = " + str(NB_NODES))

APP = 'fr.inria.rsommerard.fougereapp/.MainActivity'
PATH = os.path.dirname(os.path.realpath(__file__))
# set weave env before launching containers
print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value
# start node containers
print("Launching androfleet as node containers...")
for i in range(int(NB_NODES)):
    time.sleep(3)
    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-node' + str(i),
    '-d', 
    '--privileged',
    #'--net','host' ,
    '-e', 'DISPLAY=' + os.environ['DISPLAY'],
    '-v', '/tmp/.X11-unix:/tmp/.X11-unix',
    '-v', '/usr/lib:/usr/lib',
    '--device', '/dev/kvm',
    '-p', '555' + str(i*2 + 4) + ':5554',
    '-p', '555' + str(i*2 + 5) + ':5555',
    '-v',PATH + '/build:/build',
     'androfleet', 'node', APP, str(i)], stdout=subprocess.PIPE)
    #process = subprocess.Popen(['docker', 'run', '--name', 'androfleet-node' + str(i), '-d','--log-driver=gelf','--log-opt' ,'gelf-address=udp://172.17.0.3:12201','--log-opt','tag="node"' ,'--privileged', 'rsommerard/androfleet', 'node', APP], stdout=subprocess.PIPE)
    output = str(process.communicate()[0], 'UTF-8')

    with open('androfleet.info', 'a+') as f:
        f.write("Node=" + output)

print(str(i + 1) + ' nodes launched.')
