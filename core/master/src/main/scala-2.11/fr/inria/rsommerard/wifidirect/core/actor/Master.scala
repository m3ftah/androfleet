package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.wifidirect.core.TestScenarii
import fr.inria.rsommerard.wifidirect.core.message._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Master(val nbNodes: Int) extends Actor {

  var nodes: Set[ActorRef] = Set()
  val scenarii: List[Scenario] = TestScenarii.getDefaultScenarii
  var tickValue: Int = -1
  val serviceDiscovery = context.actorSelection("akka.tcp://ServiceDiscoverySystem@10.32.0.43:2552/user/servicediscovery")
  val interval = 2

  override def receive: Receive = initialize(0)

  def process(): Receive = {
    case Tick => tick()
    case u: Any => dealWithUnknown("process", u.getClass.getSimpleName)
  }

  def initialize(nbReadyNodes: Int): Receive = {
    case h: Hello => hello(h)
    case Ready => ready(nbReadyNodes)
    case u: Any => dealWithUnknown("initialize", u.getClass.getSimpleName)
  }

  private def hello(h: Hello): Unit = {
    //println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")

    sender ! Hello("Master")

    if (h.name == "Node") {
      sender ! scenarii(nodes.size)
      nodes += sender
    }
  }

  private def ready(nbReadyNodes: Int): Unit = {
    //println(s"[${Calendar.getInstance().getTime}] Received Ready from ${sender.path.address.host.get} (${nbReadyNodes + 1}/$nbNodes)")

    val nbReady = nbReadyNodes + 1
    if (nbReady == nbNodes) {
      context.become(process())
      println(s"[${Calendar.getInstance().getTime}] Starting process with $interval minutes between each tick")
      context.system.scheduler.schedule(0 seconds, interval minutes, self, Tick)
    } else {
      context.become(initialize(nbReady))
    }
  }

  private def tick(): Unit = {
    tickValue += 1

    println(s"[${Calendar.getInstance().getTime}] Tick: $tickValue")

    nodes.foreach(n => n ! Tick(tickValue))
    serviceDiscovery ! Tick(tickValue)
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
