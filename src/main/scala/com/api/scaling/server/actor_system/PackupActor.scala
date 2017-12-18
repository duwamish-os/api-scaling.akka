package com.api.scaling.server.actor_system

import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class PackupActor(implicit actorSystem: ActorSystem) {
  implicit val executionContext = ExecutionContext.Implicits.global

  val shipActor = actorSystem.actorOf(Props[ShipActor], "packup-actor")

  println("Scheduling " + shipActor.path.name)
  actorSystem.scheduler.schedule(0 seconds, 1 minutes, shipActor, "ShipEvent")

}
