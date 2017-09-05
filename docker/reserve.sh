google-chrome https://api.grid5000.fr/sid/ui/visualizations/gantt.html?width=800&height=600
docker-machine create -d g5k --g5k-username lmeftah --g5k-password "L@;$<ma1" --g5k-site lille --g5k-walltime "24:00:00" --engine-opt graph=/tmp/docker --g5k-resource-properties "cluster = 'chifflet'" machine-test
