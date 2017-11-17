FROM ubuntu:xenial
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>
#EXPOSE 5037 5554 5555
RUN apt-get update && \
  apt-get install -y software-properties-common python-software-properties && \
  add-apt-repository ppa:openjdk-r/ppa && \
  apt-get update && \
  apt-get install -y openjdk-8-jre \
  wget \
  curl \
  iproute2 \
  redir \
  telnet \
  unzip \
  net-tools \
  netcat\
  iputils-ping \
  htop && \
  apt-get clean && \
  rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

#adb
ENV ANDROID_HOME=/opt

ENV PATH=$ANDROID_HOME/platform-tools:$PATH

RUN mkdir -pm 0750 ~/.android $ANDROID_HOME \
    && curl -fsSL https://dl.google.com/android/repository/platform-tools-latest-linux.zip \
        -o /tmp/adb.zip ; unzip /tmp/adb.zip -d $ANDROID_HOME && \
    rm -rf /tmp/*



#Scala
RUN wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz \
 && tar xzf scala-2.11.8.tgz \
 && rm -f scala-2.11.8.tgz

ENV SCALA_HOME /scala-2.11.8
ENV PATH ${PATH}:${SCALA_HOME}/bin

ENV ANDROID_ADB_SERVER_PORT 5039

EXPOSE 54421
# ADD build /build/
ENTRYPOINT ["/build/entrypoint.sh"]
