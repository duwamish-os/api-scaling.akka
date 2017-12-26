package com.api.scaling.server.blocking

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

case class ShipEvent(item: String)

case class PackupEvent(item: String)

case class PickupEvent(item: String)

case class PickedupEvent(item: String)

case class PackedupEvent(item: String)

case class ShippedEvent(item: String)

class ShipActor(packupActor: ActorRef) extends Actor {

  println("ShipActor: " + Thread.currentThread().getName)

  implicit val timeout: Timeout = Timeout(9 seconds)

  override def receive: PartialFunction[Any, Unit] = {
    case e: ShipEvent =>
      val replyTo = sender()
      println(s"[INFO] ShipActor received $e")
      (packupActor ? PackupEvent(e.item)).map {
        case e: PackedupEvent =>
          println(s"       ShipActor received $e")
          replyTo ! ShippedEvent(e.item)
      }

  }

}

class PackupActor(pickupActor: ActorRef) extends Actor {

  println("PackupActor: " + Thread.currentThread().getName)

  implicit val timeout: Timeout = Timeout(7 seconds)

  override def receive: PartialFunction[Any, Unit] = {
    case e: PackupEvent =>
      val replyTo = sender()
      println(s"[INFO] PackupActor received $e from $replyTo")
      (pickupActor ? PickupEvent(e.item)).map {
        case e: PickedupEvent =>
          println(s"       PackupActor received $e")
          replyTo ! PackedupEvent(e.item)
      }
  }
}

class PickupActor extends Actor {

  println("PickupActor: " + Thread.currentThread().getName)

  override def receive: PartialFunction[Any, Unit] = {
    case e: PickupEvent =>
      println(s"[INFO] PickupActor received $e from ${sender()}")
      Thread.sleep(1000)
      sender() ! PickedupEvent(e.item)
  }
}

object Warehouse extends App {

  println(s"Warehouse app: ${Thread.currentThread().getName}")
  val actorSystem = ActorSystem("blocking-warehouse-actor-system")

  val pickup = actorSystem.actorOf(Props(new PickupActor()), "pickup")
  val packup = actorSystem.actorOf(Props(new PackupActor(pickup)), "packup")
  val ship = actorSystem.actorOf(Props(new ShipActor(packup)), "ship")

  implicit val timeout: Timeout = Timeout(10 seconds)

  println("[INFO] sending shipment request")

  (ship ? ShipEvent("Parka")).map {
    case ShippedEvent(item) => println(item + " is shipped")
  }
}
