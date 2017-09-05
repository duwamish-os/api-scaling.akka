package com.api.scaling.server

import akka.actor.Actor

class EventsStatsActor extends Actor {

  var cache = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case word: String =>
      val length = cache.get(word) match {

        case Some(x) => x

        case None =>
          val x = word.length
          cache += (word -> x)
          x
      }

      sender() ! length
  }
}
