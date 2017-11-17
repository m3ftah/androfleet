#!/bin/bash
echo "Starting Androfleet-emulator"
# ME=$(ip route | grep 'src 192.168.49.' | awk '{print $NF;exit}')

# echo "WEAVE_IP: $ME"
echo "role: $1"
echo "NodeNumber $2"
echo "AppPort: $3"
emulatorName="emulator-5554"
echo "Emulator Address: $emulatorName"
adb devices

redir --cport 5039  --caddr localhost --lport 5039 --laddr androfleet-emu$2  &

#redir --cport 5555 --caddr localhost --lport 5555 --laddr androfleet-emu$2 &
#
redir --cport 5554 --caddr localhost --lport 5554 --laddr androfleet-emu$2 &

redir --cport $3 --caddr localhost --lport $3 --laddr androfleet-emu$2 &

sleep 5



cp /build/config.ini $ANDROID_HOME/.android/avd/nexus.avd/

echo 'Starting emulator...'


emulator64-x86 @nexus -noaudio -no-window -gpu off -qemu -usbdevice tablet -vnc :0 &


#Waiting for adb to connect to device
echo "Waiting for adb to connect to device..."
FAIL2='1'
FAIL_COUNTER2=0
until [[ "$FAIL2" =~ '1' ]]; do
  echo "$emulatorName"
  adb connect "$emulatorName"
  FAIL2='1'
  if ! adb devices | grep "$emulatorName.*device" ; then
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
    #echo "waiting for device to boot $FAIL_COUNTER"
    if [[ $FAIL_COUNTER -gt 120 ]]; then
      echo '  Failed to start emulator'
      #exit 1
    fi
    # sleep 20
  fi
  sleep 1
done
echo 'Emulator started'

echo 'Adding emulator redirections...'
echo "" > ~/.emulator_console_auth_token


(echo "redir add tcp:11131:11131"; sleep 1; echo 'exit') | telnet localhost 5554
(echo "redir add tcp:$3:$3"; sleep 1; echo 'exit') | telnet localhost 5554



#Waiting for emulator to start
echo "Ping androfleet-node$2..."
FAIL3=''
FAIL_COUNTER3=0
until [[ "$FAIL3" =~ '1' ]]; do
  if ping -c 1 -W 1 "androfleet-node$2"; then
    FAIL3='1'
    echo "androfleet-node$2 is alive!"
  else
    echo "ping didn't work for androfleet-node$2..."
    FAIL3=''
    let 'FAIL_COUNTER3 += 1'
    if [[ $FAIL_COUNTER3 -gt 120 ]]; then
      echo "Failed to connect androfleet-node$2 !!!"
      exit 1
    fi
    sleep 20
  fi
done

echo "redir to androfleet-node$2:54421"

redir --cport 54421 --caddr androfleet-node$2 --lport 54421 --laddr localhost &

#echo 'Installing the apk...'

#adb -s $emulatorName -e install -r /build/app-debug.apk
#adb -s $emulatorName -e logcat -c
#echo 'Launching application...'
#adb -s $emulatorName -e shell am start -n $2
#echo 'Waiting for App to start...'


adb -e logcat -v time Fougere:V APP:V WiDi:V *:S
