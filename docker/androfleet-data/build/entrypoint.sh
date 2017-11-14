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

    # redir --cport 5039 --caddr localhost --lport 5039 --laddr $(ip addr list eth1 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1) &
    # redir --cport 5039 --caddr localhost --lport 5039 --laddr androfleet-master &
    # for i in $(seq 1 $(($2)));
    # do
    #   echo "$(($i + 2800))"
    #   redir --cport $(($i + 2800)) --caddr localhost --lport $(($i + 2800)) --laddr $(ip addr list eth1 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1) &
    # done
    #
    # adb devices

    /build/androfleet-master-1.0/bin/androfleet-master $2 &
    tail -f /dev/null
    ;;

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

    redir --cport 5039 --caddr localhost --lport 5039 --laddr $(ip addr list eth1 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1) &
    redir --cport 5039 --caddr localhost --lport 5039 --laddr androfleet-node$3 &


    adb devices

    sleep 5

#Waiting for adb to connect to device
    echo "Waiting for adb to connect to device..."
    FAIL2='1'
    FAIL_COUNTER2=0
    until [[ "$FAIL2" =~ '1' ]]; do
      echo "$ME:5555"
      adb connect "$ME:5555"
      FAIL2='1'
      if ! adb devices | grep "$ME:5555.*device" ; then
        #echo "failed to connect device $FAIL_COUNTER2"
        FAIL2=''
        let 'FAIL_COUNTER2 += 1'
        if [[ $FAIL_COUNTER2 -gt 120 ]]; then
          echo ' Failed to connect device!!!!!!!!'
        fi
        sleep 20
      fi
    done

#Waiting for emulator to start
    echo 'Waiting for emulator to complete boot...'
    BOOT_COMPLETED=''
    FAIL_COUNTER=0
    until [[ "$BOOT_COMPLETED" =~ '1' ]]; do
      BOOT_COMPLETED=`adb -s $ME:5555 shell getprop sys.boot_completed 2>&1`
      if [[ "$BOOT_COMPLETED" =~ 'not found' ]]; then
        let 'FAIL_COUNTER += 1'
        if [[ $FAIL_COUNTER -gt 120 ]]; then
          echo '  Failed to start emulator'
          #exit 1
        fi
      fi
      sleep 1
    done



    echo 'Starting Scala program'
    /build/androfleet-node-1.0/bin/androfleet-node $2 $ME $3 'emulator-5554' '5555'



    tail -f /dev/null

    #adb -e logcat -v time Fougere:V APP:V WiDi:V *:S
    ;;

  'servicediscovery' )
    /build/androfleet-servicediscovery-1.0/bin/androfleet-servicediscovery &
    tail -f /dev/null
    ;;

  * )
    echo "$MODE is not recognized as ContainerType"
    exit ;;
esac
