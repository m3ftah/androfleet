package fr.inria.rsommerard.wifidirect

import java.util.Calendar

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.ServiceDiscovery

object Main extends App {

  val system = ActorSystem("ServiceDiscoverySystem", ConfigFactory.load("servicediscovery"))
  val serviceDiscovery = system.actorOf(Props[ServiceDiscovery], "servicediscovery")

  println(s"[${Calendar.getInstance().getTime}] ServiceDiscovery actor started...")
}
