#!/usr/bin/python3

import os
import subprocess


print('Stopping running nodes...')
process = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
lines = output.strip().split('\n')
for l in lines:
    if ('androfleet-node' in l):
        container_id = l.split()[0]
        process = subprocess.Popen(['docker', 'kill', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Removing stopped containers...')
process = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
lines = output.strip().split('\n')
for l in lines:
    if ('androfleet-node' in l):
        container_id = l.split()[0]
        process = subprocess.Popen(['docker', 'rm', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Done.')
