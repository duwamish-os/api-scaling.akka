package com.api.scaling.server

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem

object EventEmitterNode {

  def main(args: Array[String]) {
    val config = ConfigFactory.load()
    val system = ActorSystem("ClusterSystem", config)

    system.actorOf(ApiClusterActor.props(config.getInt("producer.metrics-interval-seconds")))

    system.actorOf(EventEmitter.props(config.getInt("producer.send-interval-millis"),
      config.getInt("producer.data-array-size")), name = "producer")
  }

}
