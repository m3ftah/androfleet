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

    redir --cport 5039 --caddr localhost --lport 5039 --laddr $(ip addr list eth1 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1) &
    redir --cport 5039 --caddr localhost --lport 5039 --laddr androfleet-master &
    for i in $(seq 1 $(($2)));
    do
      echo "$(($i + 2800))"
      redir --cport $(($i + 2800)) --caddr localhost --lport $(($i + 2800)) --laddr $(ip addr list eth1 | grep 'inet ' | cut -d ' ' -f6 | cut -d/ -f1) &
    done

    adb devices

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

    #apt-get update
    #apt-get -y  install iptables

    #sysctl -w net.ipv4.conf.ethwe.route_localnet=1


    #iptables -t nat -I PREROUTING -p tcp -d $ME/23 --dport 1:65535 -j DNAT --to-destination 127.0.0.1


    redir --cport 5039 --caddr androfleet-master --lport 5039 --laddr localhost &

    redir --cport 5555 --caddr localhost --lport 5555 --laddr $ME &

    redir --cport $4 --caddr localhost --lport $4 --laddr $ME &

    sleep 5

    cp /build/config.ini $ANDROID_HOME/.android/avd/nexus.avd/

    echo 'Starting emulator...'
    #export SDL_VIDEO_X11_VISUALID=0x022

    #emulator64-x86 @nexus -noaudio -no-window -gpu off -verbose -qemu -usbdevice tablet -vnc :0 &
    emulator64-x86 @nexus -noaudio -no-window -gpu off -qemu -usbdevice tablet -vnc :0 &

    #$ANDROID_HOME/tools/emulator${EMULATOR} -avd ${NAME} -no-window -no-audio
    #emulator64-x86 -avd Androidx86 -no-skin -no-audio -no-window -no-boot-anim -noskin -gpu off -port 5554 -no-cache  -memory 512 -partition-size 200 &

    if [ 0 -eq 0 ]; then

#Waiting for adb to connect to device
    echo "Waiting for adb to connect to device..."
    FAIL2=''
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
    echo 'Emulator started'

    echo 'Adding emulator redirections...'
    echo "" > ~/.emulator_console_auth_token

    #65535


       #(
       #for i in {1..65535}
       #do
       #echo "redir add tcp:$i:898$i"; sleep 1;
       #done
       #echo 'exit') | telnet localhost 5554


    (echo "redir add tcp:11131:11131"; sleep 1; echo 'exit') | telnet localhost 5554
    (echo "redir add tcp:$4:$4"; sleep 1; echo 'exit') | telnet localhost 5554

    #echo 'Installing the apk...'

    #adb -s $ME:5555 -e install -r /build/app-debug.apk
    #adb -s $ME:5555 -e logcat -c
    #echo 'Launching application...'
    #adb -s $ME:5555 -e shell am start -n $2
    #echo 'Waiting for App to start...'


    echo 'Starting Scala program'
    /build/androfleet-node-1.0/bin/androfleet-node $2 $ME $3 $ME '5555' &

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