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
RUN echo "y" | android update sdk --no-ui --force -a --filter android-15,sys-img-x86-android-15,platform-tools
#-t android-19 --abi armeabi-v7a --skin WXGA720
#RUN echo "n" | android create avd --force -n nexus -t android-23 -b default/x86
RUN resize2fs /opt/android-sdk-linux/system-images/android-15/default/x86/userdata.img 100M
RUN echo "n" | android create avd --force -n nexus -t android-15 -b default/x86



#Androfleet
RUN apt-get update && apt-get -y install \
  #wget \
  iproute2 \
  redir \
  telnet \
  unzip \
  ruby-full \
  net-tools \
  netcat \
  iputils-ping \
  htop &&\
  apt-get clean && \
  rm -rf /var/lib/apt/lists/*


RUN mkdir /opt/android-sdk-linux/tools/keymaps && \
    touch /opt/android-sdk-linux/tools/keymaps/en-us

ENV ANDROID_ADB_SERVER_PORT 5039
EXPOSE 5039 5554 5555

#ADD skins /build/skins
ADD config.ini /build/
ADD entrypoint.sh /build/
#ADD build /build/
ENTRYPOINT ["/build/entrypoint.sh"]
