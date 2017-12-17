package com.api.scaling.server

import akka.actor.{Actor, ActorRef, ReceiveTimeout}

class EventStatsAggregator(event: String, aggregateCondition: Int, replyTo: ActorRef) extends Actor {

  var resultAggregationState: IndexedSeq[Int] = IndexedSeq.empty[Int]

  import scala.concurrent.duration._

  context.setReceiveTimeout(3.seconds)

  def receive: PartialFunction[Any, Unit] = {

    case wordCount: Int =>
      resultAggregationState = resultAggregationState :+ wordCount

      if (resultAggregationState.size == aggregateCondition) {

        println(s"[INFO] EventStatsAggregator mean aggregation is completed for $aggregateCondition words")

        val meanWordLength = resultAggregationState.sum.toDouble / resultAggregationState.size

        replyTo ! StatsResultNotification(event, meanWordLength)

        context.stop(self)
      }

    case ReceiveTimeout =>
      replyTo ! EventFailed("Service unavailable, try again later")
      context.stop(self)
  }
}
