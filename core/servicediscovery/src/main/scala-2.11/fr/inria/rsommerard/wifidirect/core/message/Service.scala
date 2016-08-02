package fr.inria.rsommerard.wifidirect.core.message

case class Service(instanceName: String, registrationType: String, fullDomainName: String, txtRecordMap: Map[String, String], srcDevice: String)

