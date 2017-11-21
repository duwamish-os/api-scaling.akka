package com.api.scaling.server.actor_system

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class ActorSystem2 {

  val actorSystem = ActorSystem("actor-system2")
  val artistActor = actorSystem.actorOf(Props[ArtistActor], "artist-actor")

  implicit val executionContext = ExecutionContext.Implicits.global

  actorSystem.scheduler.schedule(0 seconds, 1 minutes, artistActor, "artist")

}


class ArtistActor extends Actor {

  override def receive = {
    case event: String =>
      println("ArtistActor " + event)
      sender() ! s"hi $event"
  }

}
