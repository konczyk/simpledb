import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SimpleDBTest extends FunSuite {

  private val testConf = ConfigFactory.parseString("akka.loglevel = OFF")
  private implicit val system = ActorSystem("TestDB", testConf)

  test("SetRequest puts key/value into the map") {
    val actor = TestActorRef(new SimpleDB)
    actor ! SetRequest("key", "val")
    val db = actor.underlyingActor

    assert(db.map.get("key") === Some("val"))
  }

}
