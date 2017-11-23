#!/usr/bin/python3

import sys
import time
import calendar
import os
import shutil
import subprocess

BASE = '/home/lakhdar/ilab/androfleet/sfcabs'

DATA = BASE + '/cabspottingdata'
GEN = BASE + '/gen'
TMP = BASE + '/tmp'
SCENARIOS = BASE + '/scenarios'
ZIP = SCENARIOS + '.zip'
TXT = SCENARIOS + '.txt'

# ./generate.py 30 20080606180000 20080606190000
CABS = 538
MIN = calendar.timegm(time.strptime(str(20080606180000), "%Y%m%d%H%M%S"))
MAX = calendar.timegm(time.strptime(str(20080607180000), "%Y%m%d%H%M%S"))

args = sys.argv[1:]
if len(args) >= 1:
    CABS = int(args[0])
if len(args) >= 2:
    MIN = calendar.timegm(time.strptime(args[1], "%Y%m%d%H%M%S"))
if len(args) >= 3:
    MAX = calendar.timegm(time.strptime(args[2], "%Y%m%d%H%M%S"))

if MAX < MIN:
    print('Error(min date > max date)')
    sys.exit(1)

print('CABS=' + str(CABS))
print('MIN=' + str(MIN))
print('MAX=' + str(MAX))

if os.path.exists(TXT):
    os.remove(TXT)
if os.path.exists(ZIP):
    os.remove(ZIP)

os.mkdir(GEN)
os.mkdir(TMP)

files = [ f for f in os.listdir(DATA) if f.startswith('new') ]

for file in files:
    traces = []
    with open(os.path.join(DATA, file), 'r') as f:
        for line in f:
            tmstmp = int(line.split()[3])
            if tmstmp >= MAX:
                continue
            if tmstmp <= MIN:
                continue

            latitude = line.split()[0]
            longitude = line.split()[1]
            timestamp = line.split()[3]

            traces.append(file + ',' + latitude + ',' + longitude + ',' + timestamp)

    if len(traces) == 0:
        continue

    with open(os.path.join(GEN , file), 'w') as f:
        f.write('name,latitude,longitude,timestamp')
        f.write('\n')
        for tr in traces:
            f.write(tr)
            f.write('\n')

generates = [f for f in os.listdir(GEN) if f.startswith('new')]

print(str(len(generates)) + ' cabs found in the period /' + str(len(files)) + ' total cabs')

tnbl = {}

for file in generates:
    with open(os.path.join(GEN, file), 'r') as f:
        lines = f.readlines()
        tnbl[file] = len(lines) - 1

tnbls = ((k, tnbl[k]) for k in sorted(tnbl, key=tnbl.get, reverse=True))

slctd = []
count = 1
for k, v in tnbls:
    if count > CABS:
        break
    count += 1
    slctd.append(k)

for sl in slctd:
    shutil.copy(os.path.join(GEN, sl), os.path.join(TMP, sl))

with open(os.path.join(TXT), 'w') as f:
    f.write('name,latitude,longitude,timestamp')
    f.write('\n')
    for sl in slctd:
        with open(os.path.join(TMP, sl), 'r') as fbis:
            lines = fbis.readlines()[1:]
            for l in lines:
                f.write(l)

subprocess.call(['zip', '-r', ZIP, TMP], stdout=open(os.devnull, 'wb'))

print(str(len(slctd)) + ' cab traces zipped (' + ZIP + ')')

if os.path.exists(GEN):
    shutil.rmtree(GEN)
if os.path.exists(TMP):
    shutil.rmtree(TMP)
