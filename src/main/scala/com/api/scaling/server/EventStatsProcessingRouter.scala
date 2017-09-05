package com.api.scaling.server

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig

class EventStatsProcessingRouter extends Actor {

  // This router is used both with lookup and deploy of routees. If you
  // have a router with only lookup of routees you can use Props.empty
  // instead of Props[EventsStatsActor.class].
  val workerRouter: ActorRef = context.actorOf(FromConfig.props(Props[EventsStatsActor]), name = "workerRouter")

  def receive: PartialFunction[Any, Unit] = {

    case StatsEvent(payload) if payload != "" =>
      val wordsInSentence = payload.split(" ")
      val replyTo = sender() // important to not close over sender()

      // create actor that collects replies from workers
      val aggregator = context.actorOf(Props(classOf[EventStatsAggregator], payload, wordsInSentence.size, replyTo))

      wordsInSentence foreach { word =>
        println("[INFO] EventStatsProcessingRouter Routing through EventsStatsActor")
        workerRouter.tell(ConsistentHashableEnvelope(word, word), aggregator)
      }
  }
}

class EventStatsAggregator(event: String, aggregateCondition: Int, replyTo: ActorRef) extends Actor {

  var resultAggregation: IndexedSeq[Int] = IndexedSeq.empty[Int]

  import scala.concurrent.duration._

  context.setReceiveTimeout(3.seconds)

  def receive = {

    case wordCount: Int =>
      resultAggregation = resultAggregation :+ wordCount

      if (resultAggregation.size == aggregateCondition) {

        println(s"[INFO] EventStatsAggregator mean aggregation is completed for ${aggregateCondition} words")

        val meanWordLength = resultAggregation.sum.toDouble / resultAggregation.size

        replyTo ! StatsResultNotification(event, meanWordLength)

        context.stop(self)
      }

    case ReceiveTimeout =>
      replyTo ! EventFailed("Service unavailable, try again later")
      context.stop(self)
  }
}
