package fr.inria.rsommerard.social.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.social.core.message._

class Social extends Actor {

  var ipNodes: Map[ActorRef, String] = Map()

  val master = context.actorSelection("akka.tcp://MasterSystem@10.32.0.42:2552/user/master")

  override def preStart() {
    master ! Hello("Social")
  }

  override def receive: Receive = {
    case h: Hello => hello(h)
    case i: IP => ip(i)
    case Request => request()
    case u: Any => dealWithUnknown("receive", u.getClass.getSimpleName)
  }

  private def request(): Unit = {
    sender ! Persons(ipNodes.values.toList)
  }

  private def ip(i: IP): Unit = {
    ipNodes += (sender -> i.value)
  }

  private def hello(h: Hello): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
