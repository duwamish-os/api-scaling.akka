package com.api.scaling.server

import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global

object EventStatsAPINode {

  def main(ports: Array[String]): Unit = {

    implicit val timeout = Timeout(100, TimeUnit.MILLISECONDS)

    val system = ActorSystem("ApiCluster")
    val statsActor = system.actorOf(Props[EventsStatsActor], name = "statsWorker")
    system.actorOf(Props[EventStatsProcessingRouter], name = "statsProcessor")

    val result = statsActor ? "ping pong"

    result.map { r =>
      println("===========")
      println(r)
      println("===========")
    }

    Thread.sleep(1000)
  }

}
