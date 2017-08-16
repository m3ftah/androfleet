#!/usr/bin/python3

import subprocess

subprocess.call(['docker', 'build', '-q', '-t', 'androfleet', '.'])
