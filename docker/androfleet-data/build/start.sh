#!/bin/bash
#v1

if [ $# -lt 1 ]
then
  echo "Usage : $0 ContainerType"
  exit
fi

MODE=$1
IP=$(ip addr list eth0 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1)

case $MODE in
  'master' )
    if [ $# -lt 2 ]
    then
      echo "Usage : $0 master NbNodes"
      exit
    fi

    /build/androfleet-master-1.0/bin/androfleet-master $2 ;;

  'node' )
    if [ $# -lt 2 ]
    then
      echo "Usage : $0 node Package/Activity"
      exit
    fi
    ME=$(ip route | grep 'src 192.168.49.' | awk '{print $NF;exit}')

    echo "WEAVE_IP: $ME"
    echo "Configuring redir for $ME..."
    echo "role: $1"
    echo "AppPackage $2"
    echo "NodeNumber $3"
    echo "AppPort: $4"


    echo 'Starting Scala program'
    /build/androfleet-node-1.0/bin/androfleet-node $2 $ME $3 'emulator-5554' '5555' &

    fi


    tail -f /dev/null

    #adb -e logcat -v time Fougere:V APP:V WiDi:V *:S
    ;;

  'servicediscovery' )
    /build/androfleet-servicediscovery-1.0/bin/androfleet-servicediscovery ;;

  * )
    echo "$MODE is not recognized as ContainerType"
    exit ;;
esac
