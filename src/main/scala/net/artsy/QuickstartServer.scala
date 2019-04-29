package net.artsy

//#quick-start-server
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object QuickstartServer extends App with CounterRoutes {

  // set up ActorSystem and other dependencies here
  implicit val system: ActorSystem                = ActorSystem("counterAkkaHttpServer")
  implicit val materializer: ActorMaterializer    = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val counterActor: ActorRef =
    system.actorOf(CounterActor.props, "counterActor")

  // this acts as the main class
  lazy val routes: Route = counterRoutes

  val serverBinding: Future[Http.ServerBinding] =
    Http().bindAndHandle(routes, "0.0.0.0", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      println(
        s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/"
      )
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  // run the server indefinitely, until killed
  Await.result(system.whenTerminated, Duration.Inf)
}
