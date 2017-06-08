package fr.inria.rsommerard.wifidirect.core.widi

object Protocol {

  val CARTON: String = "CARTON"

  val ACK: String = "ACK"

  val HELLO: String = "HELLO"

  val DISCOVER_PEERS: String = "DISCOVER_PEERS"
  val STOP_DISCOVERY: String = "STOP_DISCOVERY"

  val DISCOVER_SERVICES: String = "DISCOVER_SERVICES"

  val REQUEST_CONNECTION_INFO: String = "REQUEST_CONNECTION_INFO"

  val REQUEST_PEERS: String = "REQUEST_PEERS"
  val CONNECT: String = "CONNECT"
  val CANCEL_CONNECT: String = "CANCEL_CONNECT"
}
