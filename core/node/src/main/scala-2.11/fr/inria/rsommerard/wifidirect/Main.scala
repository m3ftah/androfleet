package fr.inria.rsommerard.wifidirect

import java.util.Calendar

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.Node
import fr.inria.rsommerard.wifidirect.core.message.Ready
import fr.inria.rsommerard.wifidirect.core.widi.Emulator

object Main extends App {

  val packageName: String = args(0).split('/')(0)
  val weaveIp: String = args(1)

  val emulator = new Emulator(weaveIp)

  val system = ActorSystem("NodeSystem", ConfigFactory.load("node"))
  val node = system.actorOf(Props(classOf[Node], weaveIp, emulator), "node")
  println(s"[${Calendar.getInstance().getTime}] Node actor started...")
  //println(s"Arg 0 :" + args(0))
  //println(s"Arg 1 :" + args(1))

  emulator.start(node)

  while (! Emulator.isApplicationStarted(packageName)) {
    Thread.sleep(3000)
  }
  Thread.sleep(30000)
  node ! Ready
}
