package fr.inria.rsommerard.wifidirect

import java.util.Calendar

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.Master
import fr.inria.rsommerard.wifidirect.core.message.{State, Tick}

import scala.io.Source

object Main extends App {

  val nbNodes: Int = args(0).toInt

  val system = ActorSystem("MasterSystem", ConfigFactory.load("master"))
  val master = system.actorOf(Props(classOf[Master], nbNodes), "master")

  println(s"[${Calendar.getInstance().getTime}] Master actor started...")

  println("Commands available [exit (e) | quit (q), state (s), tick (t)]")
  for (ln <- Source.stdin.getLines()) {
    val input = ln.toLowerCase
    if (input == "quit" || input == "q" || input == "exit" || input == "e") {
      System.exit(0)
    } else if (input == "tick" || input == "t") {
      master ! Tick
    } else if (input == "state" || input == "s") {
      master ! State
    }
  }
}
