package com.api.scaling.server.actor_system

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSelection, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object WarehouseApp {

  implicit val eventSystem: ActorSystem = ActorSystem("warehouse-system")

  PickupActor.apply()
  new PackupActor()

  val pickupActor: ActorSelection = eventSystem.actorSelection("user/" + "pickup-actor")

  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    println("========================================================")
    pickupActor ! "Lamb of God album"

    pickupActor.ask("Shirts").onComplete {
      case Success(event) => println("[Consumer]- " + event)
      case Failure(ex) => println(ex)
    }
    println("=========================================================")
  }

}
