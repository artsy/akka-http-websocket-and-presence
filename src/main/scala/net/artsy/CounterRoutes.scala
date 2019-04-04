package net.artsy

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.ask
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import net.artsy.CounterActor._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

trait CounterRoutes {
  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  def counterActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val counterRoutes: Route =
    pathPrefix("counter") {
      concat(
        path("plus") {
          get {
            counterActor ? Plus
            complete(StatusCodes.OK, "plus")
          }
        },
        path("minus") {
          get {
            counterActor ? Minus
            complete(StatusCodes.OK, "minus")
          }
        },
        path("summary") {
          get {
            onComplete((counterActor ? Summary).mapTo[Long]) {
              case Success(value) => complete(value.toString)
              case Failure(ex) => complete((StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}"))
            }
          }
        })
    }
}
