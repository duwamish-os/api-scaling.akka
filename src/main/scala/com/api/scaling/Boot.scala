package com.api.scaling

import com.api.scaling.client.EventStatsClientActor
import com.api.scaling.server.EventStatsServer

object Boot {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      EventStatsServer.main(Array("2551", "2552", "0"))
      EventStatsClientActor.main(Array.empty)
    } else {
      EventStatsServer.main(args)
    }
  }
}
