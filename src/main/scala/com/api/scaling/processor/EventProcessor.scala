package com.api.scaling.processor

import akka.actor.{Actor, Props, UnboundedStash, ActorLogging}
import scala.concurrent.duration.DurationInt

class EventProcessor(val processingTimeMillis: Int) extends Actor with UnboundedStash with ActorLogging {

  import context.dispatcher

  def receive: Receive = {

    case data: Array[Int] => {
      println(s"[INFO] EventProcessor processing ${data} will take ${processingTimeMillis}")
      context.become(processing, discardOld = false)
      context.system.scheduler.scheduleOnce(processingTimeMillis.millis, self, "endProcessing")
    }

  }

  def processing: Receive = {

    case data: Array[Int] => stash()

    case "endProcessing" => {
      log.debug("endProcessing") // for unit test

      unstashAll()
      context.unbecome()
    }
  }

}

object EventProcessor {

  def props(processingTimeMillis: Int): Props = Props(new EventProcessor(processingTimeMillis))

}
