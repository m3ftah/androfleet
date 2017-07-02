# androfleet

AndroFleet is a WiFi Direct large-scale testing framework for Android apps.

## Requirement

- Docker (https://docs.docker.com/engine/installation/)
- Weave network (https://www.weave.works/install-weave-net/)

## Default static Weave IP

- Master: 192.168.48.3
- ServiceDiscovery: 192.168.48.2

## Build

### 1. Build core

```bash
cd docker
./build-core.py
```

### 2. Build androfleet image

```bash
cd docker
./build-image.py
```

## Launch Androfleet with 2 Android Emulators

```bash
cd docker
./experiment.py 2
```
# Using Androfleet Gradle Plugin
For faster integration with your app, you can use our Gradle Plugin available in this link : [android-p2p-androfleetplugin](https://github.com/rtoin/android-p2p-androfleetplugin)
