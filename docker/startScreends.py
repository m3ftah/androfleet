#!/usr/bin/python3

import subprocess
import os
import sys
import time
import socket
import fcntl
import struct


NB_NODES = sys.argv[1]
for i in range(int(NB_NODES)):
    process = subprocess.Popen(['docker', 'inspect', '-f', "'{{.Node.IP}}'", 'androfleet-node' + str(i)], stdout=subprocess.PIPE)
    output = str(process.communicate()[0], 'UTF-8')
    nodeRemoteAddress = output[1:-2]
    nodeRemotePort = '50' + str(i + 1).zfill(3)
    print(nodeRemoteAddress + ':' + nodeRemotePort)
    subprocess.Popen(['vncviewer', nodeRemoteAddress + ':' + nodeRemotePort])
