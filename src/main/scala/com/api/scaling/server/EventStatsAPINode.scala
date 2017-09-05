package com.api.scaling.server

import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Address
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.RelativeActorPath
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus

object EventStatsAPINode {

  def main(ports: Array[String]): Unit = {

    ports foreach { port =>
      // Override the configuration of the port when specified as program argument
      val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=" + port)
        .withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
        .withFallback(ConfigFactory.load("application"))

      val system = ActorSystem("ApiCluster", config)

      system.actorOf(Props[EventsStatsActor], name = "statsWorker")
      system.actorOf(Props[EventStatsProcessingRouter], name = "statsProcessor")
    }
  }

}
