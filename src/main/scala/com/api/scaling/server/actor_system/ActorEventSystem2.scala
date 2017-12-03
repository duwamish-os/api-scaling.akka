package com.api.scaling.server.actor_system

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class ActorEventSystem2(implicit actorSystem: ActorSystem) {
  implicit val executionContext = ExecutionContext.Implicits.global

  val artistActor = actorSystem.actorOf(Props[ArtistEventActor], "artist-actor")

  println("Scheduling ActorEventSystem2# " + artistActor.path.name)
  actorSystem.scheduler.schedule(0 seconds, 1 minutes, artistActor, "ArtistEvent")

}

class ArtistEventActor extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case event: String =>
      println("ActorSystem2#ArtistEventActor received " + event)
      sender() ! s"[ArtistEventActor] emitting $event"
  }

}
