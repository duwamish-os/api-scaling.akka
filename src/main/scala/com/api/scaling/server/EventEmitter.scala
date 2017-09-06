package com.api.scaling.server

import scala.concurrent.duration.DurationInt

import akka.actor.{ Actor, Props }
import akka.routing.FromConfig

class EventEmitter(val sendIntervalMillis: Int, val dataArraySize: Int) extends Actor {

  import context.dispatcher
  context.system.scheduler.schedule(1.second, sendIntervalMillis.millis, self, "send")

  val consumerRouter = context.actorOf(FromConfig.props(), name = "consumerRouter")
  val data = Array.range(0, dataArraySize)

  def receive: Receive = {

    case "send" =>
      println(s"[INFO] EventEmitter emitting ${data}")
      consumerRouter ! data

  }

}

object EventEmitter {

  def props(sendIntervalMillis: Int, dataArraySize: Int): Props =
    Props(new EventEmitter(sendIntervalMillis, dataArraySize))

}
