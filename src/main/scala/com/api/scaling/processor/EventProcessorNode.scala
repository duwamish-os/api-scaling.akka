package com.api.scaling.processor

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem

object EventProcessorNode {

  val clusterRole = "consumer"

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString(s"""akka.cluster.roles = [ "$clusterRole" ]""")
        .withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)

    system.actorOf(EventProcessor.props(config.getInt("consumer.processing-time-millis")), name = clusterRole)
  }

}
