# androfleet

AndroFleet is a large-scale emulation platform that supports WiFi-Direct interactions.

## Requirement

- Docker (https://docs.docker.com/engine/installation/)
- Weave network (https://www.weave.works/install-weave-net/)

## Build

### 1. Build android-emulator

```bash
cd docker/android-emulator
./build.py
```

### 2. Build androfleet-base

```bash
cd docker/androfleet-base
./build.py
```

### 3. Build androfleet

```bash
cd docker/androfleet
./build.py
```

## Launch experiment

```bash
cd experiment
./master.py 2
```

```bash
cd experiment
./servicediscovery.py
./node.py 2
```

### Launch ui

```bash
cd experiment
./ui.py
```

```bash
cd map
npm install
npm start
```