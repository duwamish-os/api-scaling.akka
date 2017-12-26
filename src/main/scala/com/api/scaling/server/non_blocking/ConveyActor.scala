package com.api.scaling.server.non_blocking

import akka.actor.Actor
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}

class ConveyActor extends Actor  with RequiresMessageQueue[BoundedMessageQueueSemantics] {

  override def receive: PartialFunction[Any, Unit] = {
    case event: String =>
      println(s"ConveyActor ${Thread.currentThread().getName} received " + event)
  }

}
