FROM ubuntu:xenial
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>
#EXPOSE 5037 5554 5555
RUN apt-get update && \
  apt-get install -y software-properties-common python-software-properties && \
  add-apt-repository ppa:openjdk-r/ppa && \
  apt-get update && \
  apt-get install -y openjdk-8-jre && \
    apt-get install -y curl libgl1-mesa-glx && \
        apt-get install -y lib32z1 lib32ncurses5 lib32stdc++6 && \
    apt-get clean
ENV ANDROID_HOME="/opt/android-sdk-linux"
ENV ANDROID_SDK_HOME="${ANDROID_HOME}"
ENV PATH="${PATH}:${ANDROID_SDK_HOME}/tools:${ANDROID_SDK_HOME}/platform-tools"
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
RUN cd /tmp && \
    curl -O https://dl.google.com/android/android-sdk_r24.4.1-linux.tgz && \
    cd /opt && tar xzf /tmp/*.tgz && rm /tmp/*.tgz
RUN echo "y" | android update sdk --no-ui --force -a --filter android-23,sys-img-x86-android-23,platform-tools
#build-tools-25.0.2
RUN echo "n" | android create avd --force -n nexus -t android-23 -b default/x86


#COPY socat.sh /usr/local/bin/socat.sh


#Androfleet
RUN apt-get update && apt-get -y install \
#  openjdk-8-jdk \
#  qemu-kvm \
  wget \
#  lib32stdc++6 lib32z1 lib32z1-dev \
  iproute2 \
  redir \
  telnet \
  unzip \
  ruby-full \
  net-tools \
  netcat\
 && apt-get clean


RUN wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz \
 && tar xzf scala-2.11.8.tgz \
 && rm -f scala-2.11.8.tgz

ENV SCALA_HOME /scala-2.11.8
ENV PATH ${PATH}:${SCALA_HOME}/bin
ENV ANDROID_ADB_SERVER_PORT 5039

ADD build /build/

#COPY entrypoint.sh /

ENTRYPOINT ["/build/entrypoint.sh"]
