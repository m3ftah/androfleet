#!/usr/bin/python3

import subprocess
import os
import sys
import time

print('Node')

NB_NODES = sys.argv[1]
APP = sys.argv[2]
PORT = sys.argv[3]

print("NB_NODES = " + str(NB_NODES))

#APP = 'fr.inria.rsommerard.fougereapp/.MainActivity'
PATH = os.path.dirname(os.path.realpath(__file__))
# set weave env before launching containers
#print("Setting weave env...")
process = subprocess.Popen(['weave', 'env'], stdout=subprocess.PIPE)
output = str(process.communicate()[0], 'UTF-8')
env = output.split(' ')[1:]
print(env)
for e in env:
    name, value = e.split('=')
    if not value.isspace():
        os.environ[name] = value
# start node containers

process2 = subprocess.Popen(['docker-machine', 'ip','machine-test'], stdout=subprocess.PIPE)
remoteAddress = ''.join(str(process2.communicate()[0], 'UTF-8').splitlines())
remotePort = '5555'

print("Remote Address is: " + remoteAddress)

print("installing redir on remote Machine: ")
subprocess.Popen(['docker-machine','ssh','machine-test','apt-get','-q','-y','install','redir']).wait()
subprocess.Popen(['docker-machine','ssh','machine-test','wget','-q','https://dl.google.com/android/repository/platform-tools-latest-linux.zip']).wait()
subprocess.Popen(['docker-machine','ssh','machine-test','apt-get','-q','-y','install','unzip']).wait()
subprocess.Popen(['docker-machine','ssh','machine-test','unzip','-o','-q','platform-tools-latest-linux.zip']).wait()
print("redir  Adb on remote Machine: ")
subprocess.Popen(['docker-machine','ssh','machine-test','redir', '--cport', '5037', '--caddr', 'localhost', '--lport', '5037', '--laddr', remoteAddress, '&'])
time.sleep(3)
print("kill Adb on this Machine: ")
subprocess.Popen(['adb', 'kill-server']).wait()
subprocess.Popen(['fuser', '-k', '-n', 'tcp', '5037']).wait()
print("start Adb on remote Machine: ")
subprocess.Popen(['docker-machine','ssh','machine-test','./platform-tools/adb','devices'])
time.sleep(3)
print("redir Adb on this Machine: ")
subprocess.Popen(['redir', '--cport', '5037', '--caddr', remoteAddress, '--lport', '5037', '--laddr', 'localhost', '&'])

print("Launching androfleet as node containers...")
for i in range(int(NB_NODES)):
    nodePort = '289' + str(i+1)
    subprocess.Popen(['fuser', '-k', '-n', 'tcp', nodePort]).wait()
    subprocess.Popen(['docker-machine','ssh','machine-test','redir', '--cport', nodePort, '--caddr', 'localhost', '--lport', nodePort, '--laddr', remoteAddress, '&'])
    subprocess.Popen(['redir', '--cport', nodePort, '--caddr', remoteAddress, '--lport', nodePort, '--laddr', 'localhost', '&'])
    #time.sleep(3)
    weaveAddress = '192.168.49.' +str(i+1)
    #remotePort = '3' + str(i).zfill(3)
    #remoteAddress = weaveAddress;

    #print("Redirecting terminal port from weave to remote machine")
    #print(process.communicate()[0], 'UTF-8')
    ###process = subprocess.Popen(['docker-machine','ssh','machine-test','redir', '--cport', '5555', '--caddr', weaveAddress, '--lport', remotePort, '--laddr', '0.0.0.0', '&'])
    #print(process.communicate()[0], 'UTF-8')

    process = subprocess.Popen(['docker', 'run',
    '--name','androfleet-node' + str(i),
    '-d',
    '--privileged',
    #'--net','host' ,
    '-e', 'DISPLAY=:1.0',
    '-e', 'WEAVE_CIDR=192.168.49.' +str(i+1) + '/23',
    '-v', '/tmp/.X11-unix:/tmp/.X11-unix',
    '-v', '/usr/lib:/usr/lib',

    '--device', '/dev/kvm',
    #'-v', '/opt/android-sdk-linux/system-images:/opt/android-sdk-linux/system-images',
    #'-p', '5554:5554',
    #'-p', '555' + str(i*2 + 4) + ':5554',
    #'-p', '555' + str(i*2 + 5) + ':5555',
    #'-v',PATH + '/build:/build',
    'androfleet', 'node', APP, str(i), PORT, remoteAddress, remotePort], stdout=subprocess.PIPE).wait()
    print('Node' + str(i))
    #process = subprocess.Popen(['docker', 'run', --name', 'androfleet-node' + str(i), '-d','--log-driver=gelf','--log-opt' ,'gelf-address=udp://172.17.0.3:12201','--log-opt','tag="node"' ,'--privileged', 'rsommerard/androfleet', 'node', APP], stdout=subprocess.PIPE)

print(str(i + 1) + ' nodes launched.')
