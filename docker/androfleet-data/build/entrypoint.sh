#!/bin/bash
#v1
if [ $# -lt 1 ]
then
  echo "Usage : $0 ContainerType"
  exit
fi

MODE=$1
#IP=$(ip addr list eth0 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1)

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


    emulatorName="emulator-5554"
    #ipAddress=$(ip route | grep 'src 192.168.50.' | awk '{print $NF;exit}')
    ipAddress=androfleet-node$2
    emulatorAddress=androfleet-emu$2
    #emulatorAddress=192.168.49.$(($2 +1))

    echo "role: $1"
    echo "NodeNumber $2"
    echo "Emulator Name: $emulatorName"
    echo "Emulator Address: $emulatorAddress"
    echo "Ip Address: $ipAddress"

    sleep 15

    redir --cport 5039 --caddr $emulatorAddress --lport 5039 --laddr localhost &
    redir --cport 11131 --caddr androfleet-emu$2 --lport 11131 --laddr androfleet-node$2 &


    sleep 15

    adb devices

#Waiting for adb to connect to device
    echo "Waiting for adb to connect to device..."
    FAIL2='1'
    FAIL_COUNTER2=0
    until [[ "$FAIL2" =~ '1' ]]; do
      echo "$emulatorName"
      adb connect "$emulatorName"
      FAIL2='1'
      if ! adb devices 2>&1 | grep "$emulatorName.*device" ; then
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
      BOOT_COMPLETED=`adb -s $emulatorName shell getprop sys.boot_completed 2>&1`
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
    JAVA_OPTS='-Xmx100m'
    /build/androfleet-node-1.0/bin/androfleet-node $2 $ipAddress $emulatorAddress $emulatorName



    tail -f /dev/null

    #adb -e logcat -v time Fougere:V APP:V WiDi:V *:S
    ;;

  'servicediscovery' )
    /build/androfleet-servicediscovery-1.0/bin/androfleet-servicediscovery
    ;;

  * )
    echo "$MODE is not recognized as ContainerType"
    exit ;;
esac
