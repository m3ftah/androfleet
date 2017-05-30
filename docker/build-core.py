#!/usr/bin/python3

import os
import subprocess
import argparse
import json
import shutil

APP = 'fougere'

THIS = "/".join(os.path.realpath(__file__).split('/')[:-1]) + "/build"
CORE = THIS + '/../../core'
MASTER = CORE + '/master'
SCENARIOS = MASTER + '/res/scenarios.txt'
NODE = CORE + '/node'
SERVDISC = CORE + '/servicediscovery'
ANDROID = THIS + '/../../android/' + APP

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

# Android
if not os.path.exists(THIS + '/app-debug.apk'):
    print(bcolors.WARNING ,"Rebuilding Android...", bcolors.ENDC)
    os.chdir(ANDROID + '/../')
    subprocess.call(['./fougere.py'])
    os.chdir(ANDROID)
    subprocess.call(['./gradlew', 'clean', 'assembleDebug'])
    shutil.copy(ANDROID + '/app/build/outputs/apk/app-debug.apk', THIS)

# Master
if not os.path.exists(THIS + '/androfleet-master-1.0'):
    print(bcolors.WARNING ,"Rebuilding Master...", bcolors.ENDC)
    os.chdir(MASTER)
    subprocess.call(['sbt', 'clean', 'universal:packageBin'])
    shutil.copy(MASTER + '/target/universal/androfleet-master-1.0.zip', THIS)
    subprocess.call(['unzip', THIS + '/androfleet-master-1.0.zip','-d', THIS])
    subprocess.call(['rm', THIS + '/androfleet-master-1.0.zip'])
if not os.path.exists(THIS + '/scenarios.txt'):
    print(bcolors.WARNING ,"Recopying scenario...", bcolors.ENDC)
    shutil.copy(SCENARIOS, THIS)

# Node
if not os.path.exists(THIS + '/androfleet-node-1.0'):
    print(bcolors.WARNING ,"Rebuilding Node...", bcolors.ENDC)
    os.chdir(NODE)
    subprocess.call(['sbt', 'clean', 'universal:packageBin'])
    shutil.copy(NODE + '/target/universal/androfleet-node-1.0.zip', THIS)
    subprocess.call(['unzip', THIS + '/androfleet-node-1.0.zip','-d', THIS])
    subprocess.call(['rm', THIS + '/androfleet-node-1.0.zip'])

# ServiceDiscovery
if not os.path.exists(THIS + '/androfleet-servicediscovery-1.0'):
    print(bcolors.WARNING ,"Rebuilding ServiceDiscovery...", bcolors.ENDC)
    os.chdir(SERVDISC)
    subprocess.call(['sbt', 'clean', 'universal:packageBin'])
    shutil.copy(SERVDISC + '/target/universal/androfleet-servicediscovery-1.0.zip', THIS)
    subprocess.call(['unzip', THIS + '/androfleet-servicediscovery-1.0.zip','-d', THIS])
    subprocess.call(['rm', THIS + '/androfleet-servicediscovery-1.0.zip'])

print("[", bcolors.OKGREEN ,"success", bcolors.ENDC, "]")
