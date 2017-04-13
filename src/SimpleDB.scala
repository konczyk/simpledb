import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable

case class SetRequest(key: String, value: Object)

class SimpleDB extends Actor {

  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) =>
      log.info("received SetRequest: key = {}, value = {}", key, value)
      map.put(key, value)
    case msg =>
      log.info("received unknown message: {}", msg)
  }

}
