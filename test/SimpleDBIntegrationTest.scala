import akka.actor.Status.{Failure, Success}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class SimpleDBIntegrationTest
  extends TestKit(ActorSystem(
    "TestDB",
    ConfigFactory
      .parseString("akka.loglevel = OFF")
      .withFallback(ConfigFactory.load())))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  private val actorRef = system.actorOf(Props[SimpleDB], name="TestActor")

  override def afterAll {
    shutdown()
  }

  "SimpleDB Actor" should {
    "respond with success on SetRequest" in {
      within(500.millis) {
        actorRef ! SetRequest("key", "val")
        expectMsg(Success)
      }
    }
  }

  "SimpleDB Actor" should {
    "respond with value from the store on GetRequest" in {
      within(500.millis) {
        actorRef ! GetRequest("key")
        expectMsg("val")
      }
    }
  }

  "SimpleDB Actor" should {
    "respond with failure on GetRequest with invalid key" in {
      within(500.millis) {
        actorRef ! GetRequest("key2")
        expectMsg(Failure(KeyNotFoundException("key2")))
      }
    }
  }

  "SimpleDB Actor" should {
    "respond with failure on unknown request" in {
      within(500.millis) {
        actorRef ! "something"
        expectMsg(Failure(UnknownRequest("something")))
      }
    }
  }

}
