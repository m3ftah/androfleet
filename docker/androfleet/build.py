#!/usr/bin/python3

import os
import subprocess
import argparse
import json
import shutil

APP = 'gpslocation'

THIS = "/".join(os.path.realpath(__file__).split('/')[:-1])
CORE = THIS + '/../../core'
MASTER = CORE + '/master'
SCENARIOS = MASTER + '/res/scenarios.txt'
NODE = CORE + '/node'
SERVDISC = CORE + '/servicediscovery'
UI = CORE + '/ui'
SOCIAL = CORE + '/social'
CONTEXTUAL = CORE + '/contextual'
ANDROID = THIS + '/../../android/' + APP

parser = argparse.ArgumentParser(prog='build.py', description='Docker androfleet container builder')
parser.add_argument('-p', '--push', action='store_true')
args = parser.parse_args()

# Android
os.chdir(ANDROID)
subprocess.call(['./gradlew', 'clean', 'assembleDebug'])
if os.path.exists(THIS + '/app-debug.apk'):
    os.remove(THIS + '/app-debug.apk')
shutil.copy(ANDROID + '/app/build/outputs/apk/app-debug.apk', THIS)

# Master
os.chdir(MASTER)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-master-1.0.zip'):
    os.remove(THIS + '/androfleet-master-1.0.zip')
shutil.copy(MASTER + '/target/universal/androfleet-master-1.0.zip', THIS)
if os.path.exists(THIS + '/scenarios.txt'):
    os.remove(THIS + '/scenarios.txt')
shutil.copy(SCENARIOS, THIS)

# Node
os.chdir(NODE)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-node-1.0.zip'):
    os.remove(THIS + '/androfleet-node-1.0.zip')
shutil.copy(NODE + '/target/universal/androfleet-node-1.0.zip', THIS)

# ServiceDiscovery
os.chdir(SERVDISC)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-servicediscovery-1.0.zip'):
    os.remove(THIS + '/androfleet-servicediscovery-1.0.zip')
shutil.copy(SERVDISC + '/target/universal/androfleet-servicediscovery-1.0.zip', THIS)

# UI
os.chdir(UI)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-ui-1.0.zip'):
    os.remove(THIS + '/androfleet-ui-1.0.zip')
shutil.copy(UI + '/target/universal/androfleet-ui-1.0.zip', THIS)

# SOCIAL
os.chdir(SOCIAL)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-social-1.0.zip'):
    os.remove(THIS + '/androfleet-social-1.0.zip')
shutil.copy(SOCIAL + '/target/universal/androfleet-social-1.0.zip', THIS)

# CONTEXTUAL
os.chdir(CONTEXTUAL)
subprocess.call(['sbt', 'clean', 'universal:packageBin'])
if os.path.exists(THIS + '/androfleet-contextual-1.0.zip'):
    os.remove(THIS + '/androfleet-contextual-1.0.zip')
shutil.copy(CONTEXTUAL + '/target/universal/androfleet-contextual-1.0.zip', THIS)


# Build AndroFleet Docker image
os.chdir(THIS)
subprocess.call(['docker', 'build', '-t', 'rsommerard/androfleet', '.'])

if args.push:
    credentials = os.path.expanduser('~/.docker/config.json')

    if os.path.exists(credentials):
        with open(credentials) as jsf:
            data = json.load(jsf)
            if len(data['auths']) == 0:
                subprocess.call(['docker', 'login'])
    else:
        subprocess.call(['docker', 'login'])

    subprocess.call(['docker', 'push', 'rsommerard/androfleet'])

# Clean
if os.path.exists(THIS + '/app-debug.apk'):
    os.remove(THIS + '/app-debug.apk')
if os.path.exists(THIS + '/androfleet-master-1.0.zip'):
    os.remove(THIS + '/androfleet-master-1.0.zip')
if os.path.exists(THIS + '/scenarios.txt'):
    os.remove(THIS + '/scenarios.txt')
if os.path.exists(THIS + '/androfleet-node-1.0.zip'):
    os.remove(THIS + '/androfleet-node-1.0.zip')
if os.path.exists(THIS + '/androfleet-servicediscovery-1.0.zip'):
    os.remove(THIS + '/androfleet-servicediscovery-1.0.zip')
if os.path.exists(THIS + '/androfleet-ui-1.0.zip'):
    os.remove(THIS + '/androfleet-ui-1.0.zip')
if os.path.exists(THIS + '/androfleet-social-1.0.zip'):
    os.remove(THIS + '/androfleet-social-1.0.zip')
if os.path.exists(THIS + '/androfleet-contextual-1.0.zip'):
    os.remove(THIS + '/androfleet-contextual-1.0.zip')
