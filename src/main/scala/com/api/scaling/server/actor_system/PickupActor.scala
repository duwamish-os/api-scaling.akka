package com.api.scaling.server.actor_system

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class PickupActor()(implicit actorSystem: ActorSystem) {

  def initialise(): PickupActor = {

    val musicActor = actorSystem.actorOf(Props[ShipActor], "pickup-actor")

    println("Scheduling ActorEventSystem1# " + musicActor.path.name)
    actorSystem.scheduler.schedule(0 seconds, 1 minutes, musicActor, "PickupEvent")
    this
  }

}

object PickupActor {
  def apply()(implicit actorSystem: ActorSystem): PickupActor = {
    new PickupActor().initialise()
  }
}
