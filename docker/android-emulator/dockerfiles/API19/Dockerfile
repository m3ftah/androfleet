FROM ubuntu:16.04

RUN apt-get update && apt-get -y install \
  openjdk-8-jdk \
  qemu-kvm \
  wget \
  && rm -rf /var/lib/apt/lists/*

RUN wget -q http://dl.google.com/android/android-sdk_r24.4.1-linux.tgz \
  && tar xzf android-sdk_r24.4.1-linux.tgz \
  && rm -f android-sdk_r24.4.1-linux.tgz

ENV ANDROID_HOME /android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools

RUN echo y | android update sdk --filter platform-tools --no-ui -a
RUN echo y | android update sdk --filter android-19 --no-ui -a
RUN echo y | android update sdk --filter sys-img-x86-android-19 --no-ui -a

RUN echo y | android update adb

RUN echo no | android create avd -n Androidx86 -t android-19

RUN sed -i -e 's/hw.ramSize=512/hw.ramSize=1024/g' /root/.android/avd/Androidx86.avd/config.ini
