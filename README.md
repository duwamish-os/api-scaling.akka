
[API Router with Pool of Remote Deployed Routees](http://developer.lightbend.com/guides/akka-sample-cluster-scala/#router-example-with-pool-of-remote-deployed-routees)
---------------------------------------------

a cluster aware router on single master node that creates and deploys workers
instead of looking them up.

```bash
sbt "runMain com.api.scaling.server.EventStatsApiProcessorPool 2551"
[INFO] [09/05/2017 15:28:14.888] [ApiCluster-akka.actor.default-dispatcher-16] [akka.tcp://ApiCluster@127.0.0.1:2551/user/statsProcessor] ClusterSingletonManager state change [Start -> Oldest]

# following node won't get involved in processing events, unless leader is
# stopped

sbt "runMain com.api.scaling.server.EventStatsApiProcessorPool 2552"
[INFO] [09/05/2017 15:31:43.220] [ApiCluster-akka.actor.default-dispatcher-3] [akka.cluster.Cluster(akka://ApiCluster)] Cluster Node [akka.tcp://ApiCluster@127.0.0.1:2552] - Welcome from [akka.tcp://ApiCluster@127.0.0.1:2551]
```

```
sbt "runMain com.api.scaling.client.EventStatsClientActor"
[INFO] [09/05/2017 10:18:23.746] [run-main-0] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:51155] - Started up successfully
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 1077,3.6)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 46,3.4)
[INFO] EventStatsClientActor StatsResultNotification(This is the text that will be analyzed - 6321,3.6)
```
