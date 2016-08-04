#!/usr/bin/python3

import os
import sys
import shutil
import subprocess

print('Reading androfleet.info file....')
if not os.path.exists('androfleet.info'):
    print('The androfleet.info file does not exist.')
    sys.exit(1)

if not os.path.exists('log'):
    print('The log folder does not exist.')
    sys.exit(1)

print('Adding Master in androfleet.info file.')
process = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'r') as f:
    content = f.read().strip()

csplit = content.split('\n')

for c in csplit:
    name, id = c.split('=')
    print('Processing ' + name + ' logs...')
    process = subprocess.Popen(['docker', 'logs', id], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output = str(process.communicate()[0], 'UTF-8')

    with open('log/' + name + '_' + id + '.log', 'w+') as f:
        f.write(output)

logs = [ f for f in os.listdir('log') if f.endswith('.log') ]

for lf in logs:
    print(lf)
    if lf.startswith('Node'):
        with open('log/' + lf, 'r') as f:
            for line in f:
                if line.startswith('WEAVE_IP'):
                    ip = line.split()[1]
                    break

        name = 'N' + ip.replace('.', '')
        shutil.move('log/' + lf, 'log/' + name)

print('Done.')
