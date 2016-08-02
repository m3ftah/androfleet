import java.util.Calendar

import akka.actor.ActorSystem
import spray.http.StatusCodes
import spray.routing.SimpleRoutingApp
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import fr.inria.rsommerard.wifidirect.core.message.{Location, Locations, UI}

import scala.concurrent.duration._
import org.json4s._
import jackson.Serialization.write
import org.json4s.jackson.Serialization

import scala.util.{Failure, Success}

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("UISystem", ConfigFactory.load("ui"))

  import system.dispatcher
  implicit val timeout = Timeout(3.second)

  val serviceDiscovery = system.actorSelection("akka.tcp://ServiceDiscoverySystem@10.32.0.43:2552/user/servicediscovery")

  lazy val statusRoute = {
    path("status") {
      get {
        complete(StatusCodes.OK)
      }
    }
  }

  lazy val positionsRoute = {
    path("locations") {
      get {
        onComplete(serviceDiscovery ? UI) {
          case Success(value) =>
            val locations: List[Location] = value.asInstanceOf[Locations].values

            implicit val formats = Serialization.formats(NoTypeHints)
            complete(write(locations))

          case Failure(ex) =>
            complete(StatusCodes.InternalServerError, s"[${Calendar.getInstance().getTime}] An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  startServer(interface = "localhost", port = 8080) {
    statusRoute ~ positionsRoute
  }
}
