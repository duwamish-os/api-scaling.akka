package com.api.scaling.server

import akka.actor.Actor

case class WordEvent(payload: String)

class EventsStatsActor extends Actor {

  var lengthCacheState = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case event: WordEvent =>

      println(s"[INFO] EventsStatsActor ${event.payload} from ${sender()}")

      val length = lengthCacheState.get(event.payload) match {

        case Some(len) => len

        case None =>
          val length = event.payload.length
          lengthCacheState += (event.payload -> length)
          length
      }

      sender() ! WordLengthEvent(length)

    case others => println(s"unhandled $others")
  }
}
