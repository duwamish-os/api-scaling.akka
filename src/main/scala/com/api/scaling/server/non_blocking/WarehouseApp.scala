package com.api.scaling.server.non_blocking

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSelection, ActorSystem}
import akka.util.Timeout
import com.api.scaling.server.blocking.PickupEvent

object WarehouseApp {

  implicit val eventSystem: ActorSystem = ActorSystem("nb-warehouse-system")

  //start actors
  PackupActorApi.init(eventSystem)
  PickupActorApi.init(eventSystem)

  val pickupActor: ActorSelection = eventSystem.actorSelection("user/" + "pickup-actor")

  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    (1 to 5).foreach { i =>
      println(s"emitting PickupEvent $i")
      pickupActor ! PickupEvent(s"Lamb of God album - $i")

      Thread.sleep(2000)
    }
  }

}
