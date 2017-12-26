package com.api.scaling.server.non_blocking

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSelection, ActorSystem}
import akka.util.Timeout
import com.api.scaling.server.blocking.PickupEvent

import scala.io.StdIn

object WarehouseApp {

  println(s"Main Thread ${Thread.currentThread().getName}")

  implicit val eventSystemKernel: ActorSystem = ActorSystem("nb-warehouse-system")

  //start actors
  PackupActorApi.init(eventSystemKernel)
  PickupActorApi.init(eventSystemKernel)

  val pickupActor: ActorSelection = eventSystemKernel.actorSelection("user/" + "pickup-actor")

  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def main(args: Array[String]): Unit = {

    warehouseWorker("prayagupd")

    println("shipping items done")

  }

  def releaseItems(): Unit = {
    (1 to 5).foreach { i =>
      println(s"emitting PickupEvent $i")
      pickupActor ! PickupEvent(s"Lamb of God album - $i")

      Thread.sleep(3000)
    }

    (6 to 10).foreach { i =>
      println(s"emitting PickupEvent $i")
      pickupActor ! PickupEvent(s"Nexus - $i")

      Thread.sleep(5000)
    }

    //main thread will die after this, but actorSystem thread will be active
    // for ever unless terminated
    for (_ <- 'a' to 'c') {
      Thread.sleep(30000)
      threadCounts(Thread.currentThread().getThreadGroup, " ")
    }
  }

  def warehouseWorker(worker: String): Unit = {

    do {
      println("Enter item to ship:")
      val input = StdIn.readLine()

      pickupActor ! PickupEvent(input)

      Thread.sleep(3000)

      //main thread will die after this, but actorSystem thread will be active
      // for ever unless terminated
      for (_ <- 'a' to 'c') {
        Thread.sleep(30000)
        threadCounts(Thread.currentThread().getThreadGroup, " ")
      }
      println("continue?(Enter/:q)")
    } while (StdIn.readLine() != ":q")

  }

  def threadCounts(group: ThreadGroup, indent: String): Unit = {

    println(indent + "Group[" + group.getName + ":" + group.getClass + "]")
    val threads = new Array[Thread](group.activeCount() * 2 + 10)
    val nt = group.enumerate(threads, false)

    // List every thread in the group
    for (i <- 0 until nt) {
      val t = threads(i)
      println(indent + "  Thread[" + t.getName + ":" + t.getState + "]")
    }

    // Recursively list all subgroups
    val groups = new Array[ThreadGroup](group.activeGroupCount() * 2 + 10)
    val ng = group.enumerate(groups, false)

    for (i <- 0 until ng) {
      threadCounts(groups(i), indent + "  ")
    }
  }

}
