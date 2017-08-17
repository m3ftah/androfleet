apt-get -y install libgl1-mesa-swx11
apt-get -y install xfce4 xfce4-goodies vnc4server iptables
echo "00000000" >/tmp/file
echo "00000000" >>/tmp/file  # note >> for append
vncpasswd </tmp/file >/tmp/vncpasswd.1 2>/tmp/vncpasswd.2
vnc4server -geometry 1920x1080 -depth 24 :1
git clone https://github.com/novnc/noVNC.git
./noVNC/utils/launch.sh --vnc localhost:5901
