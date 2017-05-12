FROM thedrhax/android-sdk:latest

# x86 emulation requires hardware acceleration
# that requires access to /dev/kvm (--device /dev/kvm)
# that requires running as root
USER root

# ADB and terminal ports of AVD
EXPOSE 5554 5555

RUN apt-get update \
 && apt-get install -y libqt5widgets5 socat supervisor \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists /var/cache/apt

ENV ABI="x86" \
    TARGET="android-24" \
    NAME="Docker_AVD" \
    EMULATOR="64-x86"

RUN echo y | $ANDROID_HOME/tools/android update sdk --filter ${TARGET} --no-ui --force -a \
 && echo y | $ANDROID_HOME/tools/android update sdk --filter sys-img-${ABI}-${TARGET} --no-ui --force -a \
 && echo y | $ANDROID_HOME/tools/android update sdk --filter platform-tools --no-ui --force -a \
 && echo no | $ANDROID_HOME/tools/android create avd -t ${TARGET} -n ${NAME} --abi ${ABI} \
 && mkdir -p $ANDROID_HOME/tools/keymaps \
 && touch $ANDROID_HOME/tools/keymaps/en-us

RUN mkdir -p /var/log/supervisor
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf
COPY socat.sh /usr/local/bin/socat.sh


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
 && apt-get clean \
 && rm -rf /var/lib/apt/lists /var/cache/apt

RUN gem install calabash-android

RUN wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz \
 && tar xzf scala-2.11.8.tgz \
 && rm -f scala-2.11.8.tgz

ENV SCALA_HOME /scala-2.11.8
ENV PATH ${PATH}:${SCALA_HOME}/bin


COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
