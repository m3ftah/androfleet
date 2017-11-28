FROM ubuntu:xenial
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>
#EXPOSE 5037 5554 5555

RUN \
  apt-get update && \
  apt-get install -y software-properties-common && \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer


# Define working directory.
#WORKDIR /data

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle


RUN apt-get update && \
  apt-get install -y software-properties-common python-software-properties \
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
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer


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

ADD build/scenarios.txt /build/scenarios.txt

ADD build/androfleet-servicediscovery-1.0 /build/androfleet-servicediscovery-1.0
ADD build/androfleet-master-1.0 /build/androfleet-master-1.0
ADD build/androfleet-node-1.0 /build/androfleet-node-1.0

ADD build/androfleet-node.androfleet-node-1.0.jar /build/androfleet-node-1.0/lib/
ADD build/androfleet-master.androfleet-master-1.0.jar /build/androfleet-master-1.0/lib/
ADD build/androfleet-servicediscovery.androfleet-servicediscovery-1.0.jar /build/androfleet-servicediscovery-1.0/lib/


ADD entrypoint.sh  build/entrypoint.sh

EXPOSE 9011

ENTRYPOINT ["/build/entrypoint.sh"]
