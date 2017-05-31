# androfleet

AndroFleet is a WiFi Direct large-scale testing framework.

## Requirement

- Docker (https://docs.docker.com/engine/installation/)
- Weave network (https://www.weave.works/install-weave-net/)

## Default static Weave IP

- Master: 10.32.0.42
- ServiceDiscovery: 10.32.0.43
- Social: 10.32.0.44

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
