package com.api.scaling.server.non_blocking

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSelection, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import com.api.scaling.server.blocking.PickupEvent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object WarehouseApp {

  implicit val eventSystem: ActorSystem = ActorSystem("nb-warehouse-system")

  //start actors
  PackupActorApi.init(eventSystem)
  PickupActorApi.init(eventSystem)

  val pickupActor: ActorSelection = eventSystem.actorSelection("user/" + "pickup-actor")

  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    println("========================================================")
    pickupActor ! PickupEvent("Lamb of God album")

    pickupActor.ask("Shirts").onComplete {
      case Success(event) => println("[Consumer]- " + event)
      case Failure(ex) => println(ex)
    }
    println("=========================================================")
  }

}
