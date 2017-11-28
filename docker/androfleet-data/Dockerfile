FROM ubuntu
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>

ADD build/scenarios.txt /build/scenarios.txt

ADD build/androfleet-servicediscovery-1.0 /build/androfleet-servicediscovery-1.0
ADD build/androfleet-master-1.0 /build/androfleet-master-1.0
ADD build/androfleet-node-1.0 /build/androfleet-node-1.0

ADD build/androfleet-node.androfleet-node-1.0.jar /build/androfleet-node-1.0/lib/
ADD build/androfleet-master.androfleet-master-1.0.jar /build/androfleet-master-1.0/lib/
ADD build/androfleet-servicediscovery.androfleet-servicediscovery-1.0.jar /build/androfleet-servicediscovery-1.0/lib/








ADD entrypoint.sh  build/entrypoint.sh

ENTRYPOINT ["/build/entrypoint.sh","node"]
