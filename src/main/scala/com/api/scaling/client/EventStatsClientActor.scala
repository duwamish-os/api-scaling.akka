package com.api.scaling.client

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.api.scaling.server.{CalculateStatsCommand, EventFailed, StatsResultNotification}

import scala.concurrent.duration._
import scala.util.Random

trait Event

case class GetStats() extends Event

class EventStatsClientActor(router: ActorRef) extends Actor {

  import context.dispatcher

  val keepEmittingGetStatEvents = context.system.scheduler.schedule(2.seconds, 2.seconds, self, GetStats())

  override def postStop(): Unit = {
    keepEmittingGetStatEvents.cancel()
  }

  def receive: PartialFunction[Any, Unit] = {

    case GetStats() =>
      router ! CalculateStatsCommand(payload = "This is the payload that will be analyzed - " + Random.nextInt(10000))

    case resultNotification: StatsResultNotification =>
      println("[INFO] EventStatsClientActor " + resultNotification)

    case failed: EventFailed =>
      println("[INFO] EventStatsClientActor " + failed)
  }

}

object EventStatsClientActor {

  def main(args: Array[String]): Unit = {
    // note that client is not a compute node, role not defined
    val system = ActorSystem("ApiCluster")
    system.actorOf(Props(classOf[EventStatsClientActor]), "api-client")
  }
}
