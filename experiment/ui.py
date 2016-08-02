#!/usr/bin/python3

import subprocess
import os
import sys
import shutil
import time

print('UI')

# start ui container
print("Launching androfleet as ui container...")
process = subprocess.Popen(['docker', 'run', '-d', '-p', '3012:8080', 'rsommerard/androfleet', 'ui'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')

with open('androfleet.info', 'a+') as f:
    f.write("UI=" + output)

time.sleep(3)
