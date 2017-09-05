package com.api.scaling.server

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.api.scaling.client.EventStatsClientActor
import com.typesafe.config.ConfigFactory

object EventStatsMasterApp {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      startup(Seq("2551", "2552", "0"))
      EventStatsMaster.main(Array.empty)
    } else {
      startup(args)
    }
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      // Override the configuration of the port when specified as program argument
      val config =
        ConfigFactory.parseString(s"akka.remote.netty.tcp.port=" + port).withFallback(
          ConfigFactory.parseString("akka.cluster.roles = [compute]")).
          withFallback(ConfigFactory.load("stats2"))

      val system = ActorSystem("ApiCluster", config)

      system.actorOf(ClusterSingletonManager.props(
        singletonProps = Props[EventStatsProcessingActor],
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system).withRole("compute")),
        name = "statsService")

      system.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/statsService",
        settings = ClusterSingletonProxySettings(system).withRole("compute")),
        name = "statsServiceProxy")
    }
  }
}

object EventStatsMaster {
  def main(args: Array[String]): Unit = {
    // note that client is not a compute node, role not defined
    val system = ActorSystem("ApiCluster")
    system.actorOf(Props(classOf[EventStatsClientActor], "/user/statsServiceProxy"), "client")
  }
}
