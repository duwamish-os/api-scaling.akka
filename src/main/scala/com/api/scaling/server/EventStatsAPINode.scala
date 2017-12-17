package com.api.scaling.server

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EventStatsAPINode(statsActor: ActorRef) {

  def onEvent(event: String): Future[Any] = {

    implicit val timeout = Timeout(100, TimeUnit.MILLISECONDS)

    val result = statsActor ? event

    result
  }

}

object EventStatsAPINode {

  implicit val actorSystem = ActorSystem("ApiCluster")

  val statsActor = actorSystem.actorOf(Props[EventsStatsActor], name = "statsWorker")

  def onEvent(str: String): Future[Any] = new EventStatsAPINode(statsActor).onEvent(str)

  def main(ports: Array[String]): Unit = {

    onEvent("ping pong").map { r =>
      println("===========")
      println(r)
      println("===========")
    }

    Thread.sleep(1000)
  }

}
