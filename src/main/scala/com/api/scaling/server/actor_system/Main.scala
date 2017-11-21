package com.api.scaling.server.actor_system

import akka.actor.ActorSystem

object Main {

  val actorSystem3 = ActorSystem("actor-system1")
  val actor = actorSystem3.actorSelection("music-actor")

  def main(args: Array[String]): Unit = {

    println("=================================")
    println(actor.pathString)
    actor ! "im music"
    println("=================================")
  }

}
