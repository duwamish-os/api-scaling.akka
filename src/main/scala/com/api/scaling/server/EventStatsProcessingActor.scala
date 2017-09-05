package com.api.scaling.server

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig

class EventStatsProcessingActor extends Actor {

  // This router is used both with lookup and deploy of routees. If you
  // have a router with only lookup of routees you can use Props.empty
  // instead of Props[StatsWorker.class].
  val workerRouter = context.actorOf(FromConfig.props(Props[EventsStatsActor]),
    name = "workerRouter")

  def receive = {
    case StatsEvent(text) if text != "" =>
      val words = text.split(" ")
      val replyTo = sender() // important to not close over sender()
    // create actor that collects replies from workers
    val aggregator = context.actorOf(Props(
      classOf[StatsAggregator], words.size, replyTo))
      words foreach { word =>
        workerRouter.tell(
          ConsistentHashableEnvelope(word, word), aggregator)
      }
  }
}

class StatsAggregator(expectedResults: Int, replyTo: ActorRef) extends Actor {
  var results = IndexedSeq.empty[Int]

  import scala.concurrent.duration._
  context.setReceiveTimeout(3.seconds)

  def receive = {
    case wordCount: Int =>
      results = results :+ wordCount
      if (results.size == expectedResults) {
        val meanWordLength = results.sum.toDouble / results.size
        replyTo ! StatsResultNotification(meanWordLength)
        context.stop(self)
      }
    case ReceiveTimeout =>
      replyTo ! EventFailed("Service unavailable, try again later")
      context.stop(self)
  }
}
