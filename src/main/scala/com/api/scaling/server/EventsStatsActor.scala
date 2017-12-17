package com.api.scaling.server

import akka.actor.Actor
import akka.pattern.pipe

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Event

case class WordEvent(data: String)

case class WordLengthEvent(data: String, length: Int)

class EventsStatsActor extends Actor {

  var cache = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case word: WordEvent =>
      println("received")
      val length = cache.get(word.data) match {

        case Some(l) => l

        case None =>
          val length = word.data.length
          cache += (word.data -> length)
          length
      }

      context.system.eventStream.publish(WordLengthEvent(word.data, length))

      println("returning")
      sender() ! WordLengthEvent(word.data, length)
      println("after sending " + sender())

    case "process" =>
      task() pipeTo sender()
  }

  def task(): Future[Int] = {

    Future {
      100
    }

  }
}
