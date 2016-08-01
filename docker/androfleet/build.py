#!/usr/bin/python3

import os
import subprocess
import argparse
import json

parser = argparse.ArgumentParser(prog='build.py', description='Docker androfleet container builder')
parser.add_argument('-p', '--push', action='store_true')
args = parser.parse_args()

subprocess.call(['docker', 'build', '-t', 'rsommerard/androfleet', '.'])

if args.push:
    credentials = os.path.expanduser('~/.docker/config.json')

    if os.path.exists(credentials):
        with open(credentials) as jsf:
            data = json.load(jsf)
            if len(data['auths']) == 0:
                subprocess.call(['docker', 'login'])

    subprocess.call(['docker', 'push', 'rsommerard/androfleet'])
