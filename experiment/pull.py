#!/usr/bin/python3

import subprocess

print('Pulling rsommerard/androfleet image....')
subprocess.call(['docker', 'pull', 'rsommerard/androfleet'])

print('Pulling mong image....')
subprocess.call(['docker', 'pull', 'mongo'])
