package com.api.scaling.server

import akka.actor.ActorSystem
import akka.testkit.TestProbe
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global

//TODO unit spec
class EventStatsAPINodeActorUnitSpec extends FunSuite {

  val actorSystem = ActorSystem("some-system")

  val testActor = new TestProbe(actorSystem)

  val eventStats = new EventStatsAPINode(testActor.ref)

  testActor.ref.tell("hello", testActor.ref)

  testActor.reply("hi")

  test("test") {
    eventStats.onEvent("events").map { x =>
      println(x)
    }
  }

}
