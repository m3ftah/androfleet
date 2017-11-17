package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.{Actor, ActorRef}
import fr.inria.rsommerard.wifidirect.core.message._

class ServiceDiscovery extends Actor {

  var locations: Map[ActorRef, Location] = Map()
  var ipNodes: Map[ActorRef, String] = Map()
  var discoverables: Map[ActorRef, Boolean] = Map()
  var services: Map[ActorRef, Service] = Map()

  val master = context.actorSelection("akka.tcp://MasterSystem@androfleet-master:2552/user/master")

  override def preStart() {
    master ! Hello("ServiceDiscovery")
  }

  override def receive: Receive = {
    case h: Hello => hello(h)
    case i: IP => ip(i)
    case UI => ui()
    case c: Connect => connect(c)
    case d: Disconnect => disconnect(d)
    case l: Location => location(l)
    case d: Discoverable => discoverable(d)
    case s: Service => service(s)
    case t: Tick => tick(t)
    case Neighbors => neighbors()
    case u: Any => dealWithUnknown("receive", u.getClass.getSimpleName)
  }

  private def service(s: Service): Unit = {
    services += (sender -> s)

    sender ! Services(services.values.toList)
  }

  private def tick(t: Tick): Unit = {
    // println(s"[${Calendar.getInstance().getTime}] Tick: ${t.value}")
  }

  private def connect(c: Connect): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Connect request: from ${c.weaveIpFrom} to ${c.weaveIpTo} with groupOwner ${c.groupOwnerIp}")

    val sel = ipNodes.filter(e => e._2 == c.weaveIpTo)

    if (sel.isEmpty) {
      println(s"[${Calendar.getInstance().getTime}] Warning: No device found")
      return
    }

    sel.head._1 ! c
  }

  private def disconnect(d: Disconnect): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Disconnect request from ${d.weaveIp}")

    val sel = ipNodes.filter(e => e._2 == d.weaveIp)

    if (sel.isEmpty) {
      println(s"[${Calendar.getInstance().getTime}] Warning: No device found")
      return
    }

    sel.head._1 ! d
  }

  private def ip(i: IP): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received IP from ${i.value}")
    ipNodes += (sender -> i.value)
  }

  private def discoverable(d: Discoverable): Unit = {
    discoverables += (sender -> d.value)
  }

  private def ui(): Unit = {
    sender ! Locations(locations.values.toList)
  }

  private def neighbors(): Unit = {
    //println(s"[${Calendar.getInstance().getTime}] Checking neighbors")
    if (!discoverables(sender)) {
      println(s"[${Calendar.getInstance().getTime}] Warning: ${sender.path.address.host.get} is not discoverable")
      return
    }

    val loc: Option[Location] = locations.get(sender)
    //println("devices ip: " + locations.keys mkString );
    //println("devices location: " + locations.values mkString );
    if (loc.isEmpty) {
      println(s"[${Calendar.getInstance().getTime}] Warning: No Location found for ${sender.path.address.host.get}")
      return
    }

    val selection: Map[ActorRef, Location] =
      locations.filter(l => discoverables.getOrElse(l._1, false) && areInRange(loc.get, l._2))

    var nghbrs: List[Neighbor] = List()
    for (a <- selection.keys) {
      val ip: String = ipNodes(a)
      //println(s"[${Calendar.getInstance().getTime}] Warning: Neighbor detected with ip: $ip for $sender")

      nghbrs = Neighbor(ip) :: nghbrs
    }
    if (nghbrs.length > 1)
    println(s"[${Calendar.getInstance().getTime}] Neighbors detected : $nghbrs for $sender")

    sender ! Neighbors(nghbrs)
  }


  private def location(loc: Location): Unit = {
    locations += (sender -> loc)
  }

  private def hello(h: Hello): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")
  }

  private def areInRange(l1: Location, l2: Location): Boolean = {
    val R: Double = 6371000 // m
    val dLat = (l2.lat - l1.lat).toRadians
    val dLon = (l2.lon - l1.lon).toRadians
    val lat1 = l1.lat.toRadians
    val lat2 = l2.lat.toRadians
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    val distance = R * c

    // current range 180m (+-600 feet)
    distance <= 180
    //true
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
