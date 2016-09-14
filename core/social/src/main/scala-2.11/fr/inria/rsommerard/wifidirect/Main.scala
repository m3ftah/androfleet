package fr.inria.rsommerard.wifidirect

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.Social

object Main extends App {

  val system = ActorSystem("SocialSystem", ConfigFactory.load("social"))
  val social = system.actorOf(Props[Social], "social")
}
