package com.api.scaling.server.non_blocking

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.api.scaling.server.blocking.{PackupEvent, PickupEvent}

import scala.language.postfixOps

class PickupActorApi()(implicit actorSystem: ActorSystem) {

  def initialise(): PickupActorApi = {

    actorSystem.actorOf(Props(new PickupActor(actorSystem)), "pickup-actor")

    this
  }

}

object PickupActorApi {
  def init(implicit actorSystem: ActorSystem): PickupActorApi =
    new PickupActorApi().initialise()
}

class PickupActor(actorSystem: ActorSystem) extends Actor {

  val packupActor: ActorSelection = actorSystem.actorSelection("user/packup-actor")

  override def receive: PartialFunction[Any, Unit] = {
    case e: PickupEvent => packupActor ! PackupEvent(e.item)
  }
}
