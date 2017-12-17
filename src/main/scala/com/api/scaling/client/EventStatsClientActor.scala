package com.api.scaling.client

import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Address
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.RelativeActorPath
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus
import com.api.scaling.server.{EventFailed, StatsEvent, StatsResultNotification}

import scala.util.Random

trait Event
case class GetStats() extends Event

class EventStatsClientActor(processorPath: String) extends Actor {
  val cluster = Cluster(context.system)

  val processorPathElements = processorPath match {
    case RelativeActorPath(elements) => elements
    case _ => throw new IllegalArgumentException(
      "servicePath [%s] is not a valid relative actor path" format processorPath)
  }

  import context.dispatcher

  val keepEmittingGetStatEvents = context.system.scheduler.schedule(2.seconds, 2.seconds, self, GetStats())

  var workerNodesState = Set.empty[Address]

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent], classOf[ReachabilityEvent])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
    keepEmittingGetStatEvents.cancel()
  }

  def receive: PartialFunction[Any, Unit] = {

    case GetStats() if workerNodesState.nonEmpty =>
      // just pick any one
      val address = workerNodesState.toIndexedSeq(ThreadLocalRandom.current.nextInt(workerNodesState.size))
      val processorActor = context.actorSelection(RootActorPath(address) / processorPathElements)
      processorActor ! StatsEvent(payload = "This is the payload that will be analyzed - " + Random.nextInt(10000))

    case result: StatsResultNotification =>
      println("[INFO] EventStatsClientActor " + result)

    case failed: EventFailed =>
      println("[INFO] EventStatsClientActor " + failed)

    case state: CurrentClusterState =>
      workerNodesState = state.members.collect {
        case m if m.hasRole("compute") && m.status == MemberStatus.Up => m.address
      }

    case MemberUp(m) if m.hasRole("compute") => workerNodesState += m.address
    case other: MemberEvent => workerNodesState -= other.member.address
    case UnreachableMember(m) => workerNodesState -= m.address
    case ReachableMember(m) if m.hasRole("compute") => workerNodesState += m.address
  }

}

object EventStatsClientActor {

  def main(args: Array[String]): Unit = {

    // note that client is not a compute node, role not defined
    val system = ActorSystem("ApiCluster")
    system.actorOf(Props(classOf[EventStatsClientActor], "/user/statsProcessorProxy"), "api-client")

  }
}
