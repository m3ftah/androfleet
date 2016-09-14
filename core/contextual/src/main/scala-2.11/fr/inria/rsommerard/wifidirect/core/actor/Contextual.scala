package fr.inria.rsommerard.wifidirect.core.actor

import java.util.Calendar

import akka.actor.Actor
import fr.inria.rsommerard.wifidirect.core.message._
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}

class Contextual extends Actor {

  val master = context.actorSelection("akka.tcp://MasterSystem@10.32.0.42:2552/user/master")

  override def preStart() {
    master ! Hello("Contextual")
  }

  override def receive: Receive = {
    case h: Hello => hello(h)
    case Initialize => initialize()
    case u: Any => dealWithUnknown("receive", u.getClass.getSimpleName)
  }

  private def initialize(): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Initialize from ${sender.path.address.host.get}")

    val mongoClient: MongoClient = MongoClient("mongodb://10.32.0.41:27017")
    val database: MongoDatabase = mongoClient.getDatabase("androfleet")

    val nodeCollection: MongoCollection[Document] = database.getCollection("node")
    nodeCollection.find().collect().subscribe((results: Seq[Document]) => {
      val contextualCollection: MongoCollection[Document] = database.getCollection("contextual")
      for (node <- results) {
        var nd = Document("name" -> node.get("name"), "ip" -> node.get("ip"))
        contextualCollection.insertOne(node).subscribe(new Observer[Completed] {
          override def onNext(result: Completed): Unit = println("Links inserted")
          override def onError(e: Throwable): Unit = println("Failed to insert node")
          override def onComplete(): Unit = {
            println("Completed node insertion")
            if (node == results.last) mongoClient.close()
          }
        })
      }
    })
  }

  private def hello(h: Hello): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Received Hello(${h.name}) from ${sender.path.address.host.get}")
  }

  private def dealWithUnknown(state: String, name: String): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Error: received unknown message ($name) during state ($state)")
  }
}
