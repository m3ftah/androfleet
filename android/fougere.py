#!/usr/bin/python3

import os
import shutil

THIS = "/".join(os.path.realpath(__file__).split('/')[:-1])
FOUGERE = THIS + '/../../fougere'
WIDI = THIS + '/../widi'

if os.path.exists(THIS + '/fougere'):
    shutil.rmtree(THIS + '/fougere')

shutil.copytree(FOUGERE, THIS + '/fougere')

# Add widi lib
shutil.copytree(WIDI, THIS + '/fougere/widi')

fpath = os.path.join(THIS + '/fougere', 'settings.gradle')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "include ':app', ':fougere'" in line:
            line = line[:-1] + ", ':widi'\n"
        f.write(line)

fpath = os.path.join(THIS + '/fougere/app', 'build.gradle')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "compile project(path: ':fougere')" in line:
            line = line + "    compile project(path: ':widi')\n"
        f.write(line)

# Replace WiFi
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere', 'Fougere.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi.WifiManager" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        if '(WifiManager) this.activity.getSystemService(Context.WIFI_SERVICE)' in line:
            line = line.replace('(WifiManager) this.activity.getSystemService(Context.WIFI_SERVICE)', 'new WifiManager()')
        f.write(line)

# Replace Wi-Fi Direct in WiFiDirect.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'WiFiDirect.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if 'android.net.wifi.p2p' in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        if '(WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE)' in line:
            line = line.replace('(WifiP2pManager) this.activity.getSystemService(Context.WIFI_P2P_SERVICE)', 'new WifiP2pManager()')
        f.write(line)

# Replace Wi-Fi Direct in ServiceDiscovery.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'ServiceDiscovery.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi.p2p" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        f.write(line)

# Replace Wi-Fi Direct in Passive.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'Passive.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi.p2p" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        f.write(line)

# Replace Wi-Fi Direct in FougereActionListener.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'FougereActionListener.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi.p2p" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        f.write(line)

# Replace Wi-Fi Direct in ConnectionHandler.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'ConnectionHandler.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        f.write(line)

# Replace Wi-Fi Direct in Active.java
fpath = os.path.join(THIS + '/fougere/fougere/src/main/java/fr/inria/rsommerard/fougere/wifidirect', 'Active.java')
with open(fpath, 'r+') as f:
    lines = f.readlines()
    f.seek(0)
    f.truncate()
    for line in lines:
        if "android.net.wifi.p2p" in line:
            line = line.replace('android', 'fr.inria.rsommerard.widi')
        f.write(line)
        