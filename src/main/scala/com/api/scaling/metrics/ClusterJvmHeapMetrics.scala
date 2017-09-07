package com.api.scaling.metrics

import scala.collection.immutable.TreeMap

/**
  * Collects JVM heap (RAM) metrics.
  */
class ClusterJvmHeapMetrics() {

  /**
    * nodeAddress -> sequence of collected heap use on node
    */
  private[this] var nodesJVMHeapUse: TreeMap[String, Seq[Long]] = TreeMap.empty

  /**
    * Updates heap use statistics for node with nodeAddress
    */
  def update(nodeAddress: String, usedMB: Long): Unit = {

    val updatedHeapUse = nodesJVMHeapUse.getOrElse(nodeAddress, Seq.empty) :+ usedMB
    nodesJVMHeapUse += nodeAddress -> updatedHeapUse

  }

  /**
    * Nodes are sorted by their node address. Nodes with no metrics receive 0 for avg heap use.
    *
    * @return current average heap use in mb for each node in the metrics:
    * node_1_avgheapuse,node_2_avgheapuse,..,node_n_avgheapuse
    */
  def calculateAverages: Iterable[Long] =
    nodesJVMHeapUse.values map (nHeapUse => if (nHeapUse.isEmpty) 0 else nHeapUse.sum / nHeapUse.length)

  def clear(): Unit =
    nodesJVMHeapUse = nodesJVMHeapUse map { case (nodeAddress, heapUse) => nodeAddress -> Seq.empty }

}
