package com.api.scaling.server

import akka.actor.Actor

class EventsStatsActor extends Actor {

  var lengthCacheState = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case word: String =>
      val length = lengthCacheState.get(word) match {

        case Some(x) => x

        case None =>
          val length = word.length
          lengthCacheState += (word -> length)
          length
      }

      sender() ! length
  }
}
