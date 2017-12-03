package com.api.scaling.server.actor_system

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Application {

  implicit val eventSystem = ActorSystem("actor-system1")

  ActorEventSystem1.apply()
  new ActorEventSystem2()

  val musicActor = eventSystem.actorSelection("user/" + "music-actor")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    println("========================================================")
    musicActor ! "Lamb of God released a new album"

    musicActor.ask("Porcupine Tree joined the application").onComplete {
      case Success(event) => println("[Consumer]- " + event)
      case Failure(ex) => println(ex)
    }
    println("=========================================================")
  }

}
