
[A Simple Cluster](https://developer.lightbend.com/guides/akka-sample-cluster-scala/#a-simple-cluster-example)

To enable cluster capabilities in Akka project, at a minimum,

**add the remote settings**, and use `akka.cluster.ClusterActorRefProvider`

```
  remote {
    log-remote-lifecycle-events = off
    netty.tcp {
      hostname = "127.0.0.1"
      port = 0
    }
  }
```

The `akka.cluster.seed-nodes` should normally also be added to your
`application.conf` file.

eg.

```
  cluster {
    seed-nodes = [
      "akka.tcp://ApiCluster@127.0.0.1:2551",
      "akka.tcp://ApiCluster@127.0.0.1:2552"]

    # auto downing is NOT safe for production deployments.
    # you may want to use it during development, read more about it in the docs.
    auto-down-unreachable-after = 10s
  }
```

server nodes
------------

```bash
sbt "runMain com.api.scaling.server.EventStatsAPINode 2551"
[INFO] [09/05/2017 10:15:22.031] [run-main-0] [akka.remote.Remoting] Remoting started; listening on addresses :[akka.tcp://ClusterSystem@127.0.0.1:2551]
```

```
sbt "runMain com.api.scaling.server.EventStatsAPINode 2552"
[INFO] [09/05/2017 10:17:25.432] [run-main-0] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2552] - Started up successfully
[INFO] [09/05/2017 10:17:25.722] [ClusterSystem-akka.actor.default-dispatcher-16] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2552] - Welcome from [akka.tcp://ClusterSystem@127.0.0.1:2551]
```

client
------

```
sbt "runMain com.api.scaling.client.EventStatsClientActor"
[INFO] [09/05/2017 10:18:23.746] [run-main-0] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:51155] - Started up successfully
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 1077,3.6)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 46,3.4)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 6321,3.6)
```

one more server node
--------------------

```
sbt "runMain com.api.scaling.server.EventStatsAPINode 0"
[INFO] [09/05/2017 14:24:23.442] [ApiCluster-akka.actor.default-dispatcher-3] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:53661] - Welcome from [akka.tcp://ApiCluster@127.0.0.1:2552]
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsProcessingRouter Routing through EventsStatsActor
[INFO] EventStatsAggregator mean aggregation is completed for 10 words
```