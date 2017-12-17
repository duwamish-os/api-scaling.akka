package com.api.scaling.server

import com.api.scaling.client.EventStatsClientActor
import org.scalatest.FunSuite

class EventStatsApiProcessorPoolSpecs extends FunSuite {

  test("starts api") {

    //start server
    EventStatsApiProcessorPool.main(Array("2551"))
    EventStatsApiProcessorPool.main(Array("2552"))

    //start client

    EventStatsClientActor.main(Array.empty[String])


    Thread.sleep(60000)
  }
}
