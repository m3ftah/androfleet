package fr.inria.rsommerard.wifidirect

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.Contextual

object Main extends App {

  val system = ActorSystem("ContextualSystem", ConfigFactory.load("contextual"))
  val contextual = system.actorOf(Props[Contextual], "contextual")
}
