#!/usr/bin/python3

import os
import subprocess
import shutil

print('Cleaning info file and log folder....')
if os.path.exists('log'):
    shutil.rmtree('log')

os.mkdir('log')

if os.path.exists('log.zip'):
    os.remove('log.zip')

filelist = [ f for f in os.listdir() if f.endswith('.log') or f.endswith('.info') ]
for f in filelist:
    os.remove(f)

print('Stopping running containers...')
process = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
lines = output.strip().split('\n')
for l in lines:
    if ('androfleet' in l) or ('weaveworks' in l):
        container_id = l.split()[0]
        process = subprocess.Popen(['docker', 'kill', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Removing stopped containers...')
process = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
lines = output.strip().split('\n')
for l in lines:
    if ('androfleet' in l) or ('weaveworks' in l):
        container_id = l.split()[0]
        process = subprocess.Popen(['docker', 'rm', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Done.')