apt-get update
apt-get install -y curl
apt install -y -t jessie-backports  openjdk-8-jre-headless ca-certificates-java
#apt-get install -y lib32z1 lib32ncurses5 lib32stdc++6
apt-get -y install \
  wget \
  iproute2 \
  redir \
  telnet \
  unzip \
  ruby-full \
  net-tools \
  netcat


wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.tgz \
 && tar xzf scala-2.11.8.tgz \
 && rm -f scala-2.11.8.tgz

echo "export SCALA_HOME=/scala-2.11.8" >> ~/.bashrc
echo "export PATH=${PATH}:/scala-2.11.8/bin" >> ~/.bashrc



apt-get -y install libgl1-mesa-swx11
apt-get -y install xfce4 xfce4-goodies vnc4server iptables
echo "00000000" >/tmp/file
echo "00000000" >>/tmp/file  # note >> for append
vncpasswd </tmp/file >/tmp/vncpasswd.1 2>/tmp/vncpasswd.2
vnc4server -geometry 3840x2560 -depth 24 :1
git clone https://github.com/novnc/noVNC.git
./noVNC/utils/launch.sh --vnc localhost:5905
