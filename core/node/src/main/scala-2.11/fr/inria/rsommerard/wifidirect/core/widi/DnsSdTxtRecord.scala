package fr.inria.rsommerard.wifidirect.core.widi

case class DnsSdTxtRecord(fullDomainName: String, txtRecordMap: Map[String, String], var srcDevice: Device)
