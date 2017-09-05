package com.api.scaling.server

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.api.scaling.client.EventStatsClientActor
import com.typesafe.config.ConfigFactory

object EventStatsApiPool {

  def main(ports: Array[String]): Unit = {

    ports foreach { port =>
      // Override the configuration of the port when specified as program argument
      val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=" + port)
        .withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
        .withFallback(ConfigFactory.load("application"))

      val system = ActorSystem("ApiCluster", config)

      system.actorOf(ClusterSingletonManager.props(
        singletonProps = Props[EventStatsProcessingRouter],
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system).withRole("compute")),
        name = "statsProcessor")

      system.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/statsProcessor",
        settings = ClusterSingletonProxySettings(system).withRole("compute")),
        name = "statsProcessorProxy")
    }
  }

}
