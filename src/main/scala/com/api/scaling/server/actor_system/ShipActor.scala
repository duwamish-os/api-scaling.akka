package com.api.scaling.server.actor_system

import akka.actor.Actor

class ShipActor extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case event: String =>
      println("ShipActor received " + event)
      sender() ! s"shipped $event"
  }

}
