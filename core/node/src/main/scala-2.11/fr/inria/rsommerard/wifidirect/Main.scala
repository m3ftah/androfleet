package fr.inria.rsommerard.wifidirect

import java.util.Calendar

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.actor.Node
import fr.inria.rsommerard.wifidirect.core.message.Ready
import fr.inria.rsommerard.wifidirect.core.widi.Emulator

object Main extends App {

  val nodeNumber : Int = args(0).toInt
  val weaveIp: String = args(1)
  val emulatorAdress: String = args(2)
  val emulatorName: String = args(3)

  val emulator = new Emulator(weaveIp,emulatorAdress,emulatorName)

  val system = ActorSystem("NodeSystem", ConfigFactory.load("node"))
  val node = system.actorOf(Props(classOf[Node], weaveIp, emulator, nodeNumber), "node")
  println(s"[${Calendar.getInstance().getTime}] Node actor started...")
  println("nodeNumber :" + nodeNumber)
  println("ipAddress :" + weaveIp)
  println("emulatorAdress :" + emulatorAdress)

  emulator.start(node)

  //while (! emulator.isApplicationStarted(packageName)) {
  //  Thread.sleep(30000)
  //}
  //Thread.sleep(15000)
  node ! Ready
}
