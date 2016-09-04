package fr.inria.rsommerard.contextual

import java.util.Calendar

import akka.actor.{ActorSystem, Props}
import spray.http.StatusCodes
import spray.routing.SimpleRoutingApp
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.contextual.core.actor.Contextual
import fr.inria.rsommerard.contextual.core.message.{Persons, Request}

import scala.concurrent.duration._
import org.json4s._
import jackson.Serialization.write
import org.json4s.jackson.Serialization

import scala.util.{Failure, Success}

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("ContextualSystem", ConfigFactory.load("contextual"))

  val contextual = system.actorOf(Props[Contextual], "contextual")

  import system.dispatcher
  implicit val timeout = Timeout(3.second)

  lazy val defaultRoute = {
      get {
        onComplete(contextual ? Request) {
          case Success(value) =>
            val ipNodes: List[String] = value.asInstanceOf[Persons].values

            implicit val formats = Serialization.formats(NoTypeHints)
            complete(write(ipNodes))

          case Failure(ex) =>
            complete(StatusCodes.InternalServerError,
              s"[${Calendar.getInstance().getTime}] An error occurred: ${ex.getMessage}")
        }
      }
  }

  startServer(interface = "localhost", port = 8080) {
    defaultRoute
  }
}
