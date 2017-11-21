package com.api.scaling.server.actor_system

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ActorSystem1 {

  val actorSystem = ActorSystem("actor-system1")
  val musicActor = actorSystem.actorOf(Props[ArtistActor], "music-actor")

  actorSystem.scheduler.schedule(0 seconds, 1 minutes, musicActor, "music")

}

class MusicActor extends Actor {

  override def receive = {
    case event: String =>
      println("MusicActor " + event)
  }

}
