#!/usr/bin/python3

import os
import subprocess
import argparse
import json
import sys

API = ["19", "22"]

parser = argparse.ArgumentParser(prog='build.py',
    description='Docker android-emulator container builder')
parser.add_argument('-api', '--api-version', type=str, default="22")
parser.add_argument('-p', '--push', action='store_true')
args = parser.parse_args()

if args.api_version not in API:
    print("Specified version not supported. Available: 19 or 22.")
    sys.exit(1)

print("Building container with API" + args.api_version)
subprocess.call(['docker', 'build', '-t', 'rsommerard/android-emulator-api' + args.api_version,
    'dockerfiles/API' + args.api_version])

if args.push:
    credentials = os.path.expanduser('~/.docker/config.json')

    if os.path.exists(credentials):
        with open(credentials) as jsf:
            data = json.load(jsf)
            if len(data['auths']) == 0:
                subprocess.call(['docker', 'login'])

    subprocess.call(['docker', 'push', 'rsommerard/android-emulator-api' + args.api_version])
