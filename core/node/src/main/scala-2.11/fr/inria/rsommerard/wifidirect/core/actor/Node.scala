package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.wifidirect.core.message._
import fr.inria.rsommerard.wifidirect.core.widi.Emulator

import scala.util.Random

class Node(val weaveIp: String, val emulator: Emulator) extends Actor {
  val master = context.actorSelection("akka.tcp://MasterSystem@10.32.0.42:2552/user/master")
  val serviceDiscovery = context.actorSelection("akka.tcp://ServiceDiscoverySystem@10.32.0.43:2552/user/servicediscovery")

  var neighbors: List[Neighbor] = List()
  var scenario: Scenario = _
  var currentScenarioIndex = -1

  override def preStart() {
    master ! Hello("Node")
    serviceDiscovery ! IP(weaveIp)
  }

  override def receive: Receive = {
    case h: Hello => hello(h)
    case Ready => ready()
    case t: Tick => tick(t)
    case d: Discoverable => discoverable(d)
    case c: Connect => connect(c)
    case d: Disconnect => disconnect(d)
    case s: Service => service(s)
    case s: Services => services(s)
    case s: Scenario => scenario(s)
    case r: Request => request(r)
    case nghbrs: Neighbors => neighbors(nghbrs)
    case u: Any => dealWithUnknown("receive", u.getClass.getSimpleName)
  }

  private def service(s: Service): Unit = {
    serviceDiscovery ! s
  }

  private def scenario(s: Scenario): Unit = {
    scenario = s
  }

  private def services(s: Services): Unit = {
    emulator.updateServices(s.values)
  }

  private def tick(t: Tick): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Tick: ${t.value}")

    updateLocation()
  }

  private def disconnect(d: Disconnect): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Disconnect: $d")

    if (d.weaveIp != weaveIp) {
      serviceDiscovery ! d
    }
  }

  private def connect(c: Connect): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Connect: $c")

    c.weaveIpTo match {
      case `weaveIp` => emulator.connectExt(c.weaveIpFrom, c.groupOwnerIp)
      case _ => serviceDiscovery ! c
    }
  }

  private def discoverable(d: Discoverable): Unit = {
    serviceDiscovery ! d
  }

  private def request(r: Request): Unit = {
    r.value match {
      case "Neighbors" => serviceDiscovery ! Neighbors
    }
  }

  private def updateLocation(): Unit = {
    currentScenarioIndex += 1
    if (currentScenarioIndex >= scenario.locations.size) {
      println(s"[${Calendar.getInstance().getTime}] Warning: No more locations to set")

      return
    }

    val ownLocation: Location = scenario.locations(currentScenarioIndex)
    Emulator.setGPSLocation(ownLocation.lat, ownLocation.lon)
    serviceDiscovery ! ownLocation
  }

  private def neighbors(nghbrs: Neighbors): Unit = {
    neighbors = nghbrs.values.filter(n => n.weaveIp != emulator.weaveIp)

    println(s"[${Calendar.getInstance().getTime}] ${neighbors.size} neighbors: $neighbors")

    emulator.updateNeighbors(neighbors)
    emulator.sendPeersChangedIntent()
  }

  private def ready(): Unit = {
    master ! Ready
  }

  private def hello(h: Hello): Unit =  {
    //println(s"[${Calendar.getInstance().getTime}] Received Hello: $h")
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
