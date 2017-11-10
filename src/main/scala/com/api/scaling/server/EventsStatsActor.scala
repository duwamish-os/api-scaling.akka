package com.api.scaling.server

import akka.actor.Actor

trait Event

case class WordEvent(data: String)
case class WordLengthEvent(data: String, length: Int)

class EventsStatsActor extends Actor {

  var cache = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case word: WordEvent =>
      val length = cache.get(word.data) match {

        case Some(x) => x

        case None =>
          val x = word.data.length
          cache += (word.data -> x)
          x
      }

      context.system.eventStream.publish(WordLengthEvent(word.data, length))
  }
}
