package com.api.scaling.client

import java.util.concurrent.ThreadLocalRandom

import akka.actor.{Actor, ActorSystem, Address, Props, RelativeActorPath, RootActorPath}
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, MemberStatus}
import com.api.scaling.server.{EventFailed, StatsEvent, StatsResultNotification}

import scala.concurrent.duration._
import scala.util.Random

class EventStatsClientActor(processorPath: String) extends Actor {
  val cluster = Cluster(context.system)

  val processorPathElements = processorPath match {
    case RelativeActorPath(elements) => elements
    case _ => throw new IllegalArgumentException(
      "servicePath [%s] is not a valid relative actor path" format processorPath)
  }

  import context.dispatcher

  val keepEmittingGetStatEvents = context.system.scheduler.schedule(2.seconds, 2.seconds, self, "GetStats")

  var workerNodes = Set.empty[Address]

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent], classOf[ReachabilityEvent])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
    keepEmittingGetStatEvents.cancel()
  }

  def receive = {

    case "GetStats" if workerNodes.nonEmpty =>
      // just pick any one
      val address = workerNodes.toIndexedSeq(ThreadLocalRandom.current.nextInt(workerNodes.size))
      val processorActor = context.actorSelection(RootActorPath(address) / processorPathElements)
      processorActor ! StatsEvent(payload = "This is the payload that will be analyzed - " + Random.nextInt(10000))

    case result: StatsResultNotification =>
      println("[INFO] EventStatsClientActor " + result)

    case failed: EventFailed =>
      println("[INFO] EventStatsClientActor " + failed)

    case state: CurrentClusterState =>
      workerNodes = state.members.collect {
        case m if m.hasRole("compute") && m.status == MemberStatus.Up => m.address
      }

    case MemberUp(m) if m.hasRole("compute") => workerNodes += m.address
    case other: MemberEvent => workerNodes -= other.member.address
    case UnreachableMember(m) => workerNodes -= m.address
    case ReachableMember(m) if m.hasRole("compute") => workerNodes += m.address
  }

}

object EventStatsClientActor {

  def main(args: Array[String]): Unit = {
    // note that client is not a compute node, role not defined
    val system = ActorSystem("ApiCluster")
    system.actorOf(Props(classOf[EventStatsClientActor], "/user/statsProcessor"), "api-client")
  }
}
