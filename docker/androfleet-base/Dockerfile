FROM rsommerard/android-emulator-api22

RUN wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz \
  && tar xzf scala-2.11.8.tgz \
  && rm -f scala-2.11.8.tgz

ENV SCALA_HOME /scala-2.11.8
ENV PATH ${PATH}:${SCALA_HOME}/bin
