#!/bin/bash

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
    ME=$(ip route | grep 'src 10.' | awk '{print $NF;exit}')
    echo "WEAVE_IP: $ME"
    echo "Configuring redir for $ME..."
    echo "Zero Argument $0"
    echo "First Argument $1"
    #androidImage=$(ls /tmp/android-unknown)
    #rm /tmp/android-unknown/$androidImage
    #ln -s /android-sdk-linux/system-images/android-22/default/x86/system.img tmp/android-unknown/$androidImage
    redir --laddr=$ME --lport=11131 --caddr=127.0.0.1 --cport=11131 &
    /build/androfleet-node-1.0/bin/androfleet-node $2 $ME $3 &
    echo 'Starting emulator[5554]...'
     /usr/bin/supervisord
    #emulator64-x86 -avd Androidx86 -no-skin -no-audio -no-window -no-boot-anim -noskin -gpu off -port 5554 -no-cache  -memory 512 -partition-size 200 &
    echo 'Waiting for emulator to start...'
    BOOT_COMPLETED=''
    FAIL_COUNTER=0
    until [[ "$BOOT_COMPLETED" =~ '1' ]]; do
      BOOT_COMPLETED=`adb -s emulator-5554 shell getprop sys.boot_completed 2>&1`
      if [[ "$BOOT_COMPLETED" =~ 'not found' ]]; then
        let 'FAIL_COUNTER += 1'
        if [[ $FAIL_COUNTER -gt 120 ]]; then
          echo '  Failed to start emulator'
          exit 1
        fi
      fi
      sleep 1
    done
    echo 'Emulator started'
    echo 'Adding emulator redirections...'
    echo "" > ~/.emulator_console_auth_token
    (echo 'auth ""'; sleep 1; echo "redir add tcp:11131:11131"; sleep 1; echo 'exit') | telnet localhost 5554
    echo 'Installing the apk...'
    adb -e install -r /app-debug.apk
    adb -e logcat -c
    echo 'Launching application...'
    adb -e shell am start -n $2
    echo 'Running...'
    adb -e logcat -v time Fougere:V APP:V WiDi:V *:S;;

  'servicediscovery' )
    /build/androfleet-servicediscovery-1.0/bin/androfleet-servicediscovery ;;

  * )
    echo "$MODE is not recognized as ContainerType"
    exit ;;
esac
