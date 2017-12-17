package com.api.scaling.server

import akka.actor.Actor

case class WordEvent(payload: String)
case class WordLengthEvent(length: Int)

class EventsStatsActor extends Actor {

  var lengthCacheState = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case event: WordEvent =>

      val length = lengthCacheState.get(event.payload) match {

        case Some(len) => len

        case None =>
          val length = event.payload.length
          lengthCacheState += (event.payload -> length)
          length
      }

      println(s"[INFO] EventsStatsActor sending ${WordLengthEvent(length)} to ${sender()}")
      sender() ! WordLengthEvent(length)

    case others => println(s"unhandled $others")
  }
}
