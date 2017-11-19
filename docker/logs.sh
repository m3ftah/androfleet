source .env

eval $(docker-machine env --swarm $CLUSTER-0)

 mkdir logs
 for (( i=0; i< $NODES; ++i));
 do
   site="$(docker inspect --format='{{.Node.Name}}' androfleet-node$i)";
   echo "Getting log for $site/androfleet-node$i"
   docker logs $site/androfleet-node$i > logs/androfleet-node$i.log;

   site="$(docker inspect --format='{{.Node.Name}}' androfleet-emu$i)";
   echo "Getting log for $site/androfleet-emu$i"
   docker logs $site/androfleet-emu$i > logs/androfleet-emu$i.log;
 done


site="$(docker inspect --format='{{.Node.Name}}' androfleet-master)";
echo "Getting log for $site/androfleet-master"
docker logs $site/androfleet-master > logs/androfleet-master.log;


site="$(docker inspect --format='{{.Node.Name}}' androfleet-servicediscovery)";
echo "Getting log for $site/androfleet-servicediscovery"
docker logs $site/androfleet-servicediscovery > logs/androfleet-servicediscovery.log;
