
[API Router with Pool of Remote Deployed Routees](http://developer.lightbend.com/guides/akka-sample-cluster-scala/#router-example-with-pool-of-remote-deployed-routees)
---------------------------------------------

a cluster aware router on single master node that creates and deploys workers
instead of looking them up.

Server
------

```bash
sbt "runMain com.api.scaling.server.EventStatsApiProcessorPool 2551"
[INFO] [09/05/2017 15:28:14.888] [ApiCluster-akka.actor.default-dispatcher-16] [akka.tcp://ApiCluster@127.0.0.1:2551/user/statsProcessor] ClusterSingletonManager state change [Start -> Oldest]

...
[WARN] [12/16/2017 22:32:38.865] [New I/O boss #3] [NettyTransport(akka://ApiCluster)] Remote connection to [null] failed with java.net.ConnectException: Connection refused: /127.0.0.1:2552

...
[INFO] [12/16/2017 22:32:44.785] [ApiCluster-akka.actor.default-dispatcher-16] [akka.tcp://ApiCluster@127.0.0.1:2551/user/statsProcessorProxy] Singleton identified at [akka://ApiCluster/user/statsProcessor/singleton]
```


following node won't get involved in processing events, unless leader is
stopped


```
sbt "runMain com.api.scaling.server.EventStatsApiProcessorPool 2552"
[INFO] [12/16/2017 22:34:24.917] [run-main-0] [akka.remote.Remoting] Starting remoting
[INFO] [12/16/2017 22:34:25.210] [run-main-0] [akka.remote.Remoting] Remoting started; listening on addresses :[akka.tcp://ApiCluster@127.0.0.1:2552]

...
[INFO] [12/16/2017 22:34:25.957] [ApiCluster-akka.actor.default-dispatcher-16] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:2552] - Welcome from [akka.tcp://ApiCluster@127.0.0.1:2551]

...
[INFO] [12/16/2017 22:34:27.492] [ApiCluster-akka.actor.default-dispatcher-4] [akka.tcp://ApiCluster@127.0.0.1:2552/user/statsProcessor] ClusterSingletonManager state change [Start -> Younger]
```

Client
------

```
sbt "runMain com.api.scaling.client.EventStatsClientActor"
[INFO] [09/05/2017 10:18:23.746] [run-main-0] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:51155] - Started up successfully
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 1077,3.6)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 46,3.4)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 6321,3.6)
```

when both server nodes leave the cluster, client will have following logs

node1 left,

```
[INFO] [12/16/2017 22:38:37.521] [ApiCluster-akka.actor.default-dispatcher-16] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:63590] - Exiting confirmed [akka.tcp://ApiCluster@127.0.0.1:2551]
```

node2 left,

```
[INFO] [12/16/2017 22:38:59.234] [ApiCluster-akka.actor.default-dispatcher-15] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:63590] - Leader is removing confirmed Exiting node [akka.tcp://ApiCluster@127.0.0.1:2552]
```