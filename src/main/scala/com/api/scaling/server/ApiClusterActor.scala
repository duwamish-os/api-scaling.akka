package com.api.scaling.server

import scala.collection.immutable.HashSet
import scala.concurrent.duration.DurationInt
import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.metrics.{ClusterMetricsChanged, ClusterMetricsExtension, NodeMetrics}
import akka.cluster.metrics.StandardMetrics.HeapMemory
import com.api.scaling.metrics.{ClusterJvmHeapMetrics, MetricsLogger}
import com.api.scaling.processor.EventProcessorNode

class ApiClusterActor(metricsIntervalSeconds: Int) extends Actor {

  import context.dispatcher
  context.system.scheduler.schedule(metricsIntervalSeconds.seconds, metricsIntervalSeconds.seconds,
    self, "LogProcessorsHeapUsage")

  private[this] val cluster = Cluster(context.system)
  private[this] val metricsLogger = new MetricsLogger(name = cluster.selfAddress.port.getOrElse(0).toString())
  private[this] val clusterJvmHeapMetrics = new ClusterJvmHeapMetrics()

  private var processors: Set[String] = HashSet.empty

  override def preStart(): Unit = {

    //subscribe to Metrics
    ClusterMetricsExtension(context.system).subscribe(self)

    cluster.subscribe(self, classOf[MemberUp])
  }

  override def postStop(): Unit = {
    ClusterMetricsExtension(context.system).unsubscribe(self)
    Cluster(context.system).unsubscribe(self)
  }

  def receive: Receive = {

    case MemberUp(m) if m.roles.contains(EventProcessorNode.clusterRole) =>
      processors += m.address.hostPort

    case ClusterMetricsChanged(clusterMetrics) =>
      clusterMetrics
        .filter(nm => processors.contains(nm.address.hostPort))
        .foreach(updateJvmHeapUse(_))

    case "LogProcessorsHeapUsage" => {
      metricsLogger.log(clusterJvmHeapMetrics.calculateAverages)
      clusterJvmHeapMetrics.clear()
    }

  }

  private[this] def updateJvmHeapUse(nodeMetrics: NodeMetrics) {
    nodeMetrics match {

      case HeapMemory(address, timestamp, used, committed, max) => {

        println(s"[INFO] ApiClusterActor \"RAM usage\": ${used.doubleValue}")

        val usedRamMB = Math.round(used.doubleValue / 1024 / 1024)
        clusterJvmHeapMetrics.update(address.hostPort, usedRamMB)

      }

      case _ => // no heap info
    }
  }

}

object ApiClusterActor {

  def props(metricsIntervalSeconds: Int): Props = Props(new ApiClusterActor(metricsIntervalSeconds: Int))

}
