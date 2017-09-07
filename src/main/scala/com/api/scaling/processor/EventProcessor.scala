package com.api.scaling.processor

import akka.actor.{Actor, Props, UnboundedStash, ActorLogging}
import scala.concurrent.duration.DurationInt

class EventProcessor(val processingTimeMillis: Int) extends Actor with UnboundedStash with ActorLogging {

  import context.dispatcher

  def receive: Receive = {

    case data: Array[Int] => {
      println(s"[INFO] EventProcessor processing ${data} will take ${processingTimeMillis}")
      context.become(processing, discardOld = false)
      context.system.scheduler.scheduleOnce(processingTimeMillis.millis, self, "PROCESSING_COMPLETED")
    }

  }

  def processing: Receive = {

    case data: Array[Int] => stash()

    case "PROCESSING_COMPLETED" => {
      log.debug("PROCESSING_COMPLETED") // for unit test

      unstashAll()
      context.unbecome()
    }
  }

}

object EventProcessor {

  def props(processingTimeMillis: Int): Props = Props(new EventProcessor(processingTimeMillis))

}
