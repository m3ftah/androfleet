#!/usr/bin/python3

import subprocess
import sys

ASK = 'Are you sure that you want to clean all these things?! (yes|no)\n'

print('This script will clean all docker containers and images!')
res = input(ASK)

while (res != 'no') and (res != 'yes'):
    res = input(ASK)

if 'no' == res:
    print('You\'ve made the good choice!')
    sys.exit(1)

print('Killing ALL running containers...')
process = subprocess.Popen(['docker', 'ps', '-q'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
if len(output) > 0:
    lines = output.strip().split('\n')
    for l in lines:
        container_id = l.split()[0]
        print('Killing ' + container_id + '...')
        process = subprocess.Popen(['docker', 'kill', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Deleting ALL stopped containers (including data-only containers)...')
process = subprocess.Popen(['docker', 'ps', '-a', '-q'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
if len(output) > 0:
    lines = output.strip().split('\n')
    for l in lines:
        container_id = l.split()[0]
        print('Deleting ' + container_id + '...')
        process = subprocess.Popen(['docker', 'rm', '-f', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Deleting ALL \'untagged/dangling\' (<none>) images...')
process = subprocess.Popen(['docker', 'images', '-q', '-f', 'dangling=true'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
if len(output) > 0:
    lines = output.strip().split('\n')
    for l in lines:
        container_id = l.split()[0]
        print('Deleting ' + container_id + '...')
        process = subprocess.Popen(['docker', 'rmi', '-f', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Deleting ALL images...')
process = subprocess.Popen(['docker', 'images', '-q'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
if len(output) > 0:
    lines = output.strip().split('\n')
    for l in lines:
        container_id = l.split()[0]
        print('Deleting ' + container_id + '...')
        process = subprocess.Popen(['docker', 'rmi', '-f', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Deleting ALL volumes...')
process = subprocess.Popen(['docker', 'volume', 'ls', '-qf', 'dangling=true'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
if len(output) > 0:
    lines = output.strip().split('\n')
    for l in lines:
        container_id = l.split()[0]
        print('Deleting ' + container_id + '...')
        process = subprocess.Popen(['docker', 'volume', 'rm', container_id], stdout=subprocess.PIPE)
        process.wait()

print('Done.')
