package fr.inria.rsommerard.wifidirect.core.widi

case class WifiP2pConfig(deviceAddress: String, wps: WpsInfo, netId: Int, groupOwnerIntent: Int)