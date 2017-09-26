# androfleet

AndroFleet is a large-scale testing framework for Android p2p apps.

## Requirement

- Docker (https://docs.docker.com/engine/installation/

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

## Launch Androfleet with 100 Android Emulators

```bash
cd docker
./try.py 100
```

# Using Androfleet Gradle Plugin
For faster integration with your app, you can use our Gradle Plugin available in this link : [android-p2p-androfleetplugin](https://github.com/m3ftah/android-p2p-androfleetplugin)


# Using Androfleet
If you are using Androfleet, please cite the following research paper:
>AndroFleet: Testing WiFi Peer-to-Peer Mobile Apps in the Large. L. Meftah, M. Gomez, R. Rouvoy, I. Chrisment - 32nd IEEE/ACM International Conference on Automated Software Engineering (ASE 2017), Oct 2017. [Youtube](https://youtu.be/gJ5_Ed7XL04), [pdf](https://hal.inria.fr/hal-01574466/)
