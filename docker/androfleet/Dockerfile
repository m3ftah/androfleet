FROM rsommerard/androfleet-base

RUN apt-get update && apt-get -y install \
  iproute2 \
  redir \
  telnet \
  unzip \
  && rm -rf /var/lib/apt/lists/*

COPY androfleet-master-1.0.zip /
COPY androfleet-node-1.0.zip /
COPY androfleet-servicediscovery-1.0.zip /
COPY androfleet-ui-1.0.zip /
# COPY androfleet-social-1.0.zip /
# COPY androfleet-contextual-1.0.zip /

RUN unzip androfleet-master-1.0.zip && rm /androfleet-master-1.0.zip
RUN unzip androfleet-node-1.0.zip && rm /androfleet-node-1.0.zip
RUN unzip androfleet-servicediscovery-1.0.zip && rm androfleet-servicediscovery-1.0.zip
RUN unzip androfleet-ui-1.0.zip && rm androfleet-ui-1.0.zip
# RUN unzip androfleet-social-1.0.zip && rm androfleet-social-1.0.zip
# RUN unzip androfleet-contextual-1.0.zip && rm androfleet-contextual-1.0.zip

COPY scenarios.txt /
COPY app-debug.apk /
COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
