package com.api.scaling.server.non_blocking

import akka.actor.Actor
import com.api.scaling.server.blocking.{ShipEvent, ShippedEvent}

class ShipActor extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case event: ShipEvent =>
      println("ShipActor received " + event)
      sender() ! ShippedEvent(event.item)
  }

}
