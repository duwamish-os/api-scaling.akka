package com.api.scaling.server.non_blocking

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.api.scaling.server.blocking.{PackupEvent, ShipEvent}

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

class PackupActorApi(implicit actorSystem: ActorSystem) {
  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global

  val packupActor: ActorRef = actorSystem.actorOf(Props(new PackupActor(actorSystem)), "packup-actor")
  val shipActor: ActorRef = actorSystem.actorOf(Props[ShipActor], "ship-actor")

}

object PackupActorApi {
  def init(implicit actorSystem: ActorSystem) = new PackupActorApi()(actorSystem)
}

class PackupActor(actorSystem: ActorSystem) extends Actor {
  println(s"Assigned PackupActor ${Thread.currentThread().getName}")

  val shipActor: ActorSelection = actorSystem.actorSelection("user/ship-actor")

  override def receive: PartialFunction[Any, Unit] = {
    case e: PackupEvent =>
      println(s"PackupActor ${Thread.currentThread().getName} received ${e}")
      shipActor ! ShipEvent(e.item)
  }
}
