FROM m3ftah/androfleet-base
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>


#ADD build/androfleet-master-1.0.zip build/androfleet-node-1.0.zip build/androfleet-servicediscovery-1.0.zip /build/
ADD build/skins /build/skins
ADD build/config.ini build/entrypoint.sh build/scenarios.txt  /build/

ADD build/androfleet-node-1.0/lib /build/androfleet-node-1.0/lib
ADD build/androfleet-master-1.0/lib /build/androfleet-master-1.0/lib
ADD build/androfleet-servicediscovery-1.0/lib /build/androfleet-servicediscovery-1.0/lib

ADD build/androfleet-servicediscovery-1.0/bin /build/androfleet-servicediscovery-1.0/bin
ADD build/androfleet-master-1.0/bin /build/androfleet-master-1.0/bin
ADD build/androfleet-node-1.0/bin /build/androfleet-node-1.0/bin

# ADD build/androfleet-node-1.0.zip /build/
# ADD build/androfleet-servicediscovery-1.0.zip /build/

#ADD build/entrypoint.sh /build/
#ADD build/app-debug.apk /build/

ENTRYPOINT ["/build/entrypoint.sh"]
