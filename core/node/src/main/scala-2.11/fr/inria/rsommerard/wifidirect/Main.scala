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
  val nodeNumber : Int = args(2).toInt

  val emulator = new Emulator(weaveIp)

  val system = ActorSystem("NodeSystem", ConfigFactory.load("node"))
  val node = system.actorOf(Props(classOf[Node], weaveIp, emulator, nodeNumber), "node")
  println(s"[${Calendar.getInstance().getTime}] Node actor started...")
  println("Arg 0 :" + args(0))
  println("Arg 1 :" + args(1))
  println("Arg 2 :" + args(2))
  println("nodeNumber :" + nodeNumber)

  emulator.start(node)

  while (! Emulator.isApplicationStarted(packageName)) {
    Thread.sleep(3000)
  }
  Thread.sleep(30000)
  node ! Ready
}
