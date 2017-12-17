package com.api.scaling.server

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory

object EventStatsApiProcessorPool {

  def main(ports: Array[String]): Unit = {

    ports foreach { port =>
      // Override the configuration of the port when specified as program argument
      val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=" + port)
        .withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
        .withFallback(ConfigFactory.load("application"))

      val apiSystem = ActorSystem("ApiCluster", config)

      apiSystem.actorOf(ClusterSingletonManager.props(
        singletonProps = Props[EventStatsProcessingRouter],
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(apiSystem).withRole("compute")),
        name = "statsProcessor")

      apiSystem.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/statsProcessor",
        settings = ClusterSingletonProxySettings(apiSystem).withRole("compute")),
        name = "statsProcessorProxy")
    }
  }

}
