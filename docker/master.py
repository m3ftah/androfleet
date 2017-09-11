#!/usr/bin/python2.7

import subprocess
import os
import sys
import time
import socket
import fcntl
import struct

def get_ip_address(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntoa(fcntl.ioctl(
        s.fileno(),
        0x8915,  # SIOCGIFADDR
        struct.pack('256s', ifname[:15])
    )[20:24])

myAddress = get_ip_address('tun0')  # '192.168.0.110'
print('Master')

NB_NODES = sys.argv[1]


#nodePort = '289' + str(i+1)
#subprocess.Popen(['fuser', '-k', '-n', 'tcp', nodePort]).wait()


#subprocess.Popen(['redir', '--cport', nodePort, '--caddr', remoteAddress, '--lport', nodePort, '--laddr', 'localhost', '&'])

# start master container
print("Launching androfleet as master container...")
subprocess.call(['docker', 'run',
'--name', 'androfleet-master',
'-p', '2800-4000:2800-4000',#Calabash ports
'-p', '5039:5039',#Adb ports
'--net', 'my-net',
 '--ip', '192.168.48.3',
 '-d', 'androfleet', 'master', NB_NODES, myAddress]).wait()


print("killing adb on this machine ")
subprocess.Popen(['fuser', '-k', '-n', 'tcp', '5037']).wait()

process = subprocess.Popen(['docker', 'inspect', '-f', "'{{.Node.IP}}'", 'androfleet-master']).wait()
output = str(process.communicate()[0], 'UTF-8')
subprocess.Popen(['redir', '--cport', '5039', '--caddr', output,'--lport', '5037', '--laddr', 'localhost', '&'])
