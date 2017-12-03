package com.api.scaling.server.actor_system

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ActorEventSystem1()(implicit actorSystem: ActorSystem) {

  def initialise(): ActorEventSystem1 = {

    val musicActor = actorSystem.actorOf(Props[ArtistEventActor], "music-actor")

    println("Scheduling ActorEventSystem1# " + musicActor.path.name)
    actorSystem.scheduler.schedule(0 seconds, 1 minutes, musicActor, "MusicEvent")
    this
  }

}

object ActorEventSystem1 {
  def apply()(implicit actorSystem: ActorSystem): ActorEventSystem1 = {
    new ActorEventSystem1().initialise()
  }
}

class MusicEventActor extends Actor {

  override def receive = {
    case event: String =>
      println("[ActorEventSystem1#MusicEventActor] received " + event)
  }

}
