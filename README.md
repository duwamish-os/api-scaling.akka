
[A Simple Router](https://doc.akka.io/docs/akka/2.5.3/scala/routing.html)

Messages can be sent via a router to efficiently route them to destination
actors, known as its routees. A Router can be used inside or outside of an actor,
and you can manage the routees yourselves or use a self contained
router actor with configuration capabilities.

//FIXME readme for simple routees pool

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

server/client
-------------

```bash
[INFO] [12/17/2017 14:03:37.458] [main] [akka.remote.Remoting] Starting remoting
[INFO] [12/17/2017 14:03:37.630] [main] [akka.remote.Remoting] Remoting started; listening on addresses :[akka.tcp://ApiCluster@127.0.0.1:52286]
[INFO] [12/17/2017 14:03:37.649] [main] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:52286] - Starting up...
[INFO] [12/17/2017 14:03:37.811] [main] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:52286] - Registered cluster JMX MBean [akka:type=Cluster]
[INFO] [12/17/2017 14:03:37.811] [main] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:52286] - Started up successfully
[WARN] [12/17/2017 14:03:37.850] [ApiCluster-akka.actor.default-dispatcher-3] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/downingProvider] Don't use auto-down feature of Akka Cluster in production. See 'Auto-downing (DO NOT USE)' section of Akka Cluster documentation.
[INFO] [12/17/2017 14:03:37.856] [ApiCluster-akka.actor.default-dispatcher-2] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:52286] - Metrics collection has started successfully
[WARN] [12/17/2017 14:03:37.917] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:03:37.917] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:03:37.918] [ApiCluster-akka.remote.default-remote-dispatcher-5] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[WARN] [12/17/2017 14:03:37.919] [ApiCluster-akka.remote.default-remote-dispatcher-13] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[INFO] [12/17/2017 14:03:37.922] [ApiCluster-akka.actor.default-dispatcher-17] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [1] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[INFO] [12/17/2017 14:03:37.922] [ApiCluster-akka.actor.default-dispatcher-17] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [2] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$k#1764225533]
[INFO] EventsStatsActor sending WordLengthEvent(1) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$k#1764225533]
[INFO] EventsStatsActor sending WordLengthEvent(36) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$k#1764225533]
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$k#1764225533]
[INFO] EventStatsAggregator mean aggregation is completed for 4 words, emitting to Actor[akka://ApiCluster/user/eventStatsProcessingRouter#-728057953]
result received word some data - 9bcca137-71bb-40bd-88ad-9d5b4c2c6007: mean 11.25
[INFO] [12/17/2017 14:03:42.873] [ApiCluster-akka.actor.default-dispatcher-15] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [3] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[INFO] [12/17/2017 14:03:42.873] [ApiCluster-akka.actor.default-dispatcher-15] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [4] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:03:47.889] [ApiCluster-akka.actor.default-dispatcher-15] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/joinSeedNodeProcess-1] Couldn't join seed nodes after [2] attempts, will try again. seed-nodes=[akka.tcp://ApiCluster@127.0.0.1:2551, akka.tcp://ApiCluster@127.0.0.1:2552]
[WARN] [12/17/2017 14:03:47.895] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:03:47.897] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:03:47.897] [ApiCluster-akka.remote.default-remote-dispatcher-4] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[INFO] [12/17/2017 14:03:47.897] [ApiCluster-akka.actor.default-dispatcher-14] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [5] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:03:47.897] [ApiCluster-akka.remote.default-remote-dispatcher-4] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[INFO] [12/17/2017 14:03:47.898] [ApiCluster-akka.actor.default-dispatcher-14] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [6] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:03:52.909] [ApiCluster-akka.actor.default-dispatcher-16] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/joinSeedNodeProcess-1] Couldn't join seed nodes after [3] attempts, will try again. seed-nodes=[akka.tcp://ApiCluster@127.0.0.1:2551, akka.tcp://ApiCluster@127.0.0.1:2552]
[WARN] [12/17/2017 14:03:52.913] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:03:52.915] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:03:52.915] [ApiCluster-akka.remote.default-remote-dispatcher-5] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[INFO] [12/17/2017 14:03:52.915] [ApiCluster-akka.actor.default-dispatcher-15] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [7] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:03:52.915] [ApiCluster-akka.remote.default-remote-dispatcher-5] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[INFO] [12/17/2017 14:03:52.915] [ApiCluster-akka.actor.default-dispatcher-15] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [8] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$l#-2102483141]
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$l#-2102483141]
[INFO] EventsStatsActor sending WordLengthEvent(36) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$l#-2102483141]
[INFO] EventsStatsActor sending WordLengthEvent(1) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$l#-2102483141]
[INFO] EventStatsAggregator mean aggregation is completed for 4 words, emitting to Actor[akka://ApiCluster/user/eventStatsProcessingRouter#-728057953]
result received word some data - 9bcca137-71bb-40bd-88ad-9d5b4c2c6007: mean 11.25
[WARN] [12/17/2017 14:03:57.930] [ApiCluster-akka.actor.default-dispatcher-15] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/joinSeedNodeProcess-1] Couldn't join seed nodes after [4] attempts, will try again. seed-nodes=[akka.tcp://ApiCluster@127.0.0.1:2551, akka.tcp://ApiCluster@127.0.0.1:2552]
[WARN] [12/17/2017 14:03:57.934] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:03:57.934] [ApiCluster-akka.remote.default-remote-dispatcher-13] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[INFO] [12/17/2017 14:03:57.936] [ApiCluster-akka.actor.default-dispatcher-14] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [9] dead letters encountered. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:03:57.937] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:03:57.938] [ApiCluster-akka.remote.default-remote-dispatcher-19] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[INFO] [12/17/2017 14:03:57.938] [ApiCluster-akka.actor.default-dispatcher-14] [akka://ApiCluster/deadLetters] Message [akka.cluster.InternalClusterAction$InitJoin$] from Actor[akka://ApiCluster/system/cluster/core/daemon/joinSeedNodeProcess-1#1676017106] to Actor[akka://ApiCluster/deadLetters] was not delivered. [10] dead letters encountered, no more dead letters will be logged. This logging can be turned off or adjusted with configuration settings 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
[WARN] [12/17/2017 14:04:02.950] [ApiCluster-akka.actor.default-dispatcher-20] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/joinSeedNodeProcess-1] Couldn't join seed nodes after [5] attempts, will try again. seed-nodes=[akka.tcp://ApiCluster@127.0.0.1:2551, akka.tcp://ApiCluster@127.0.0.1:2552]
[WARN] [12/17/2017 14:04:02.955] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:04:02.957] [ApiCluster-akka.remote.default-remote-dispatcher-5] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[WARN] [12/17/2017 14:04:02.957] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:04:02.957] [ApiCluster-akka.remote.default-remote-dispatcher-19] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[WARN] [12/17/2017 14:04:07.970] [ApiCluster-akka.actor.default-dispatcher-15] [akka.tcp://ApiCluster@127.0.0.1:52286/system/cluster/core/daemon/joinSeedNodeProcess-1] Couldn't join seed nodes after [6] attempts, will try again. seed-nodes=[akka.tcp://ApiCluster@127.0.0.1:2551, akka.tcp://ApiCluster@127.0.0.1:2552]
[WARN] [12/17/2017 14:04:07.975] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552
[WARN] [12/17/2017 14:04:07.977] [ApiCluster-akka.remote.default-remote-dispatcher-4] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2552-1] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2552] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2552]] Caused by: [Connection refused: /127.0.0.1:2552]
[WARN] [12/17/2017 14:04:07.977] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2551
[WARN] [12/17/2017 14:04:07.978] [ApiCluster-akka.remote.default-remote-dispatcher-19] [akka.tcp://ApiCluster@127.0.0.1:52286/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2FApiCluster%40127.0.0.1%3A2551-0] Association with remote system [akka.tcp://ApiCluster@127.0.0.1:2551] has failed, address is now gated for [5000] ms. Reason: [Association failed with [akka.tcp://ApiCluster@127.0.0.1:2551]] Caused by: [Connection refused: /127.0.0.1:2551]
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$m#2038527281]
[INFO] EventStatsProcessingRouter Routing to EventsStatsActor
[INFO] EventsStatsActor sending WordLengthEvent(4) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$m#2038527281]
[INFO] EventsStatsActor sending WordLengthEvent(1) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$m#2038527281]
[INFO] EventsStatsActor sending WordLengthEvent(36) to Actor[akka://ApiCluster/user/eventStatsProcessingRouter/$m#2038527281]
[INFO] EventStatsAggregator mean aggregation is completed for 4 words, emitting to Actor[akka://ApiCluster/user/eventStatsProcessingRouter#-728057953]
result received word some data - 9bcca137-71bb-40bd-88ad-9d5b4c2c6007: mean 11.25
```