package com.api.scaling.server.router

import akka.actor.{Actor, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import com.api.scaling.server._

class EventStatsProcessingRouter extends Actor {

  val workerRouter: Router = Router(RoundRobinRoutingLogic(), Vector.fill(10) {
    val routee = context.actorOf(Props[EventsStatsActor])
    context watch routee
    ActorRefRoutee(routee)
  })

  def receive: PartialFunction[Any, Unit] = {

    case CalculateStatsCommand(payload) if payload != "" =>
      val wordsInSentence = payload.split(" ")

      // create actor that collects replies from workers
      val nextActor = context.actorOf(Props(classOf[EventStatsAggregator], payload, wordsInSentence.size, context.self))

      wordsInSentence foreach { word =>
        println("[INFO] EventStatsProcessingRouter Routing to EventsStatsActor")
        workerRouter.route(WordEvent(word), nextActor)
      }

    case StatsResultNotification(word, mean) => println(s"result received word $word: mean $mean")
  }
}
