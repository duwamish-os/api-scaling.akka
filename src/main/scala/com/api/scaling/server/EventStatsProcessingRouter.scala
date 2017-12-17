package com.api.scaling.server

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig

class EventStatsProcessingRouter extends Actor {

  // This router is used both with lookup and deploy of routees. If you
  // have a router with only lookup of routees you can use Props.empty
  // instead of Props[EventsStatsActor]
  val workerRouter: ActorRef = context.actorOf(FromConfig.props(Props[EventsStatsActor]), name = "workerRouter")

  def receive: PartialFunction[Any, Unit] = {

    case StatsEvent(payload) if payload != "" =>
      val wordsInSentence = payload.split(" ")
      val replyTo = sender() // important to not close over sender()

      // create actor that collects replies from workers
      val aggregator = context.actorOf(Props(classOf[EventStatsAggregator], payload, wordsInSentence.size, replyTo))

      wordsInSentence foreach { word =>
        println("[INFO] EventStatsProcessingRouter Routing through EventsStatsActor")
        workerRouter.tell(ConsistentHashableEnvelope(message = WordEvent(word), hashKey = word), aggregator)
      }

    case others => println(s"received $others")

  }
}
