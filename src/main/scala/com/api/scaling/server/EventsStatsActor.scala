package com.api.scaling.server

import akka.actor.Actor

import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.pattern.pipe

import scala.concurrent.ExecutionContext.Implicits.global

trait Event

case class WordEvent(data: String)

case class WordLengthEvent(data: String, length: Int)

class EventsStatsActor extends Actor {

  var cache = Map.empty[String, Int]

  def receive: PartialFunction[Any, Unit] = {

    case word: WordEvent =>
      println("received")
      val length = cache.get(word.data) match {

        case Some(x) => x

        case None =>
          val x = word.data.length
          cache += (word.data -> x)
          x
      }

      context.system.eventStream.publish(WordLengthEvent(word.data, length))

      println("returning")
      sender() ! WordLengthEvent(word.data, length)
      println("after sending " + sender())

    case "process" =>
      future() pipeTo sender()
  }

  def future(): Future[Int] = {

    Future {
      100
    }

  }
}
