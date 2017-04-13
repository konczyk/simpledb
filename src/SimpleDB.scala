import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging

import scala.collection.mutable

case class SetRequest(key: String, value: Object)
case class GetRequest(key: String)
case class KeyNotFoundException(key: String) extends Exception
case class UnknownRequest(req: Any) extends Exception

class SimpleDB extends Actor {

  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) =>
      log.info("received SetRequest: key = {}, value = {}", key, value)
      map.put(key, value)
      sender() ! Status.Success
    case GetRequest(key) =>
      log.info("received GetRequest: key = {}", key)
      map.get(key).fold(sender() ! Status.Failure(KeyNotFoundException(key))) {
        sender() ! _
      }
    case req => sender() ! Status.Failure(UnknownRequest(req))
  }

}

object Main extends App {
  val system = ActorSystem("simpledb")
  system.actorOf(Props[SimpleDB], name="SimpleDB")
}
