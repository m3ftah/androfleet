source .env

for (( i=1; i< $NODES; ++i)); do adb connect 192.168.49.$i; done
