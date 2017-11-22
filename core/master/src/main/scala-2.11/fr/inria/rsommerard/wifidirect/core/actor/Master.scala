package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.wifidirect.core.Scenarios
import fr.inria.rsommerard.wifidirect.core.message._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Master(val nbNodes: Int) extends Actor {

  var nodes: Set[ActorRef] = Set()
  //val scenarios: List[Scenario] = Scenarios.get
  val firstTick: Int = Scenarios.getMinTimestamp - 60
  val lastTick: Int = Scenarios.getMaxTimestamp + 60
  var tickValue: Int = firstTick
  var start: Boolean = false
  val serviceDiscovery = context.actorSelection("akka.tcp://ServiceDiscoverySystem@androfleet-servicediscovery:2552/user/servicediscovery")
  //val social = context.actorSelection("akka.tcp://SocialSystem@10.32.0.44:2552/user/social")
  //val contextual = context.actorSelection("akka.tcp://ContextualSystem@10.32.0.45:2552/user/contextual")
  val interval = 1000
  var nbReadyNodes: Int = 0


  override def receive: Receive = initialize()

  def process(): Receive = {
    case Tick => tick()
    case State => state("Process")
    case u: Any => dealWithUnknown("process", u.getClass.getSimpleName)
  }

  def initialize(): Receive = {
    case h: Hello => hello(h)
    case i: IP => ip(i)
    case State => state("Initialize")
    case Ready => ready()
    case u: Any => dealWithUnknown("initialize", u.getClass.getSimpleName)
  }

  private def ip(i: IP): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received IP(${i.value}) from ${sender.path.address.host.get}")

    //val scenar: Scenario = scenarios(nodes.size)
    nodes += sender
    //sender ! scenar
    start = (i.value == "androfleet-node0");

  }

  private def state(s: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] $s mode")
    if (s == "Initialize") {
      println(s"[${Calendar.getInstance().getTime}] Waiting ${nbNodes - nbReadyNodes} nodes")
    }
  }

  private def hello(h: Hello): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")

    sender ! Hello("Master")
  }

  private def ready(): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Ready from ${sender.path.address.host.get} (${nbReadyNodes + 1}/$nbNodes)")

    nbReadyNodes += 1
    //return
    // if (nbReadyNodes != nbNodes) {
    //   return
    // }
    if (start){
      Thread.sleep(15000)
      context.become(process())
      println(s"[${Calendar.getInstance().getTime}] Starting process with $interval milliseconds between each tick")
      println(s"[${Calendar.getInstance().getTime}] firstTick $firstTick, lastTick $lastTick")
      context.system.scheduler.schedule(0 second, interval milliseconds, self, Tick)
    }
    // context.become(process())
    // println(s"[${Calendar.getInstance().getTime}] Starting process with $interval milliseconds between each tick")
    // println(s"[${Calendar.getInstance().getTime}] firstTick $firstTick, lastTick $lastTick")
    // context.system.scheduler.schedule(0 second, interval milliseconds, self, Tick)
  }

  private def tick(): Unit = {
    if (tickValue == lastTick) {
      println(s"[${Calendar.getInstance().getTime}] Last tick reached: $tickValue")
      return
    }

    tickValue += 1
    var percentage: Double = (tickValue.toDouble - firstTick.toDouble) / (lastTick.toDouble - firstTick.toDouble) * 100
    println(s"[${Calendar.getInstance().getTime}] Tick: $tickValue, $lastTick, $percentage%")

    nodes.foreach(n => n ! Tick(tickValue))
    serviceDiscovery ! Tick(tickValue)
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
