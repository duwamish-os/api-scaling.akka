package com.api.scaling.server.non_blocking

import akka.actor.Actor
import com.api.scaling.server.blocking.{ShipEvent, ShippedEvent}

class ShipActor extends Actor {

  println(s"Assigned ShipActor ${Thread.currentThread().getName}")

  override def receive: PartialFunction[Any, Unit] = {
    case event: ShipEvent =>
      println(s"ShipActor ${Thread.currentThread().getName} received " + event)
      sender() ! ShippedEvent(event.item)
  }

}
