package com.api.scaling.server.non_blocking

import akka.actor.Actor

class ConveyActor extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case event: String => println("ConveyActor received " + event)
  }

}
