package com.api.scaling.server

import akka.actor.{Actor, ActorRef, ReceiveTimeout}

class EventStatsAggregator(word: String, aggregateCondition: Int, replyTo: ActorRef) extends Actor {

  var aggregationState: IndexedSeq[Int] = IndexedSeq.empty[Int]

  import scala.concurrent.duration._

  context.setReceiveTimeout(3.seconds)

  def receive: PartialFunction[Any, Unit] = {

    case event: WordLengthEvent =>
      aggregationState = aggregationState :+ event.length

      if (aggregationState.size == aggregateCondition) {

        println(s"[INFO] EventStatsAggregator mean aggregation is completed for $aggregateCondition words, emitting to $replyTo")

        val meanWordLength = aggregationState.sum.toDouble / aggregationState.size

        replyTo ! StatsResultNotification(word, meanWordLength)

        context.stop(self)
      }

    case ReceiveTimeout =>
      replyTo ! EventFailed("Service unavailable, try again later")
      context.stop(self)
  }
}
