package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.wifidirect.core.TestScenarii
import fr.inria.rsommerard.wifidirect.core.message._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Master(val nbNodes: Int) extends Actor {

  var nodes: Set[ActorRef] = Set()
  val scenarii: List[Scenario] = TestScenarii.get
  var tickValue: Int = -1
  val serviceDiscovery = context.actorSelection("akka.tcp://ServiceDiscoverySystem@10.32.0.43:2552/user/servicediscovery")
  val interval = 2
  var nbReadyNodes: Int = 0

  override def receive: Receive = initialize()

  def process(): Receive = {
    case Tick => tick()
    case State => state("Process")
    case u: Any => dealWithUnknown("process", u.getClass.getSimpleName)
  }

  def initialize(): Receive = {
    case h: Hello => hello(h)
    case State => state("Initialize")
    case Ready => ready()
    case u: Any => dealWithUnknown("initialize", u.getClass.getSimpleName)
  }

  private def state(s: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] $s mode")
    if (s == "Initialize") {
      println(s"[${Calendar.getInstance().getTime}] Waiting ${nbNodes - nbReadyNodes} nodes")
    }
  }

  private def hello(h: Hello): Unit = {
    //println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")

    sender ! Hello("Master")

    if (h.name == "Node") {
      sender ! scenarii(nodes.size)
      nodes += sender
    }
  }

  private def ready(): Unit = {
    //println(s"[${Calendar.getInstance().getTime}] Received Ready from ${sender.path.address.host.get} (${nbReadyNodes + 1}/$nbNodes)")

    nbReadyNodes += 1
    if (nbReadyNodes != nbNodes) {
      return
    }

    context.become(process())
    println(s"[${Calendar.getInstance().getTime}] Starting process with $interval minutes between each tick")
    context.system.scheduler.schedule(0 seconds, interval minutes, self, Tick)
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
