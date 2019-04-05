package net.artsy

//#user-routes-spec
//#test-top
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

//#set-up
class UserRoutesSpec
  extends WordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with CounterRoutes {
  //#test-top

  // Here we need to implement all the abstract members of UserRoutes.
  // We use the real UserRegistryActor to test it while we hit the Routes,
  // but we could "mock" it by implementing it in-place or by using a TestProbe()
  override val counterActor: ActorRef =
    system.actorOf(CounterActor.props, "userRegistry")

  lazy val routes = counterRoutes

  "CounterRoutes" should {
    "be able to increment and decrement the counter" in {
      // note that there's no need for the host part in the uri:
      val summaryRequest = HttpRequest(uri = "/counter/summary")

      summaryRequest ~> routes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldBe "0"
      }

      val plusRequest = HttpRequest(uri = "/counter/plus")

      plusRequest ~> routes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldBe "1"
      }

      plusRequest ~> routes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldBe "2"
      }

      val minusRequest = HttpRequest(uri = "/counter/minus")

      minusRequest ~> routes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldBe "1"
      }
    }

  }
}
