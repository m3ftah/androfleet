docker run -it --name androfleet-node0 --privileged --net=host -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /usr/lib:/usr/lib  --device /dev/kvm -p 5554:5554 -p 5555:5555 -v $(pwd)/build:/build androfleet node "fr.sommerard.fougereapp/.MainActivity" 0