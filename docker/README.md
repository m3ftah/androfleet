To run Androfleet on grid5000 cluster, you have to follow several steps:
- Make sure you are connected to grid5000 VPN: 'execVpn.sh'
- Provision with `docker-g5k` driver
  - ```
  ./docker-g5k create-cluster \
  --g5k-username "lmeftah" \
  --g5k-password "L@;$<ma1" \
  --g5k-reserve-nodes "lyon:4" \
  --engine-opt "lyon-{0..3}:graph=/tmp/docker" \
  --g5k-walltime "08:00:00" \
  --swarm-standalone-enable \
  --swarm-master "lyon-0"\
  --g5k-image "ubuntu16.04-x64-min@gfieni"```
- Build image on all the cluster sites : `./sites.py`
- Before running `Androfleet`, make sure you have the right `App Package Name` and `Connection PORT` configured in `try.py` script.
- You can run the **Androfleet** using `./try.py 10` to run with 10 nodes, this will :
  - Create an `Overlay network` called `my-net`.
  - 
