package com.api.scaling.server

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import com.api.scaling.server.router.EventStatsProcessingRouter
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

object EventStatsAPINode {

  def main(ports: Array[String]): Unit = {

    val apiSystem = ActorSystem("ApiCluster")

    val router = apiSystem.actorOf(Props[EventStatsProcessingRouter], name = "eventStatsProcessingRouter")

    apiSystem.scheduler.schedule(2.seconds, 15.seconds, router, CalculateStatsCommand("some data - " + UUID.randomUUID()))

  }

}
