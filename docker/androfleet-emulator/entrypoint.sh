echo "Starting Androfleet-emulator"
ME=$(ip route | grep 'src 192.168.49.' | awk '{print $NF;exit}')

echo "WEAVE_IP: $ME"
echo "Configuring redir for $ME..."
echo "role: $1"
echo "AppPackage $2"
echo "NodeNumber $3"
echo "AppPort: $4"

redir --cport 5039 --caddr androfleet-node$3 --lport 5039 --laddr localhost &

redir --cport 5555 --caddr localhost --lport 5555 --laddr $ME &

redir --cport 5554 --caddr localhost --lport 5554 --laddr $ME &

redir --cport $4 --caddr localhost --lport $4 --laddr $ME &

sleep 5

cp /build/config.ini $ANDROID_HOME/.android/avd/nexus.avd/

echo 'Starting emulator...'


emulator64-x86 @nexus -noaudio -no-window -gpu off -qemu -usbdevice tablet -vnc :0 &

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


(echo "redir add tcp:11131:11131"; sleep 1; echo 'exit') | telnet localhost 5554
(echo "redir add tcp:$4:$4"; sleep 1; echo 'exit') | telnet localhost 5554

#echo 'Installing the apk...'

#adb -s $ME:5555 -e install -r /build/app-debug.apk
#adb -s $ME:5555 -e logcat -c
#echo 'Launching application...'
#adb -s $ME:5555 -e shell am start -n $2
#echo 'Waiting for App to start...'


adb -e logcat -v time Fougere:V APP:V WiDi:V *:S
