
[API Router with Pool of Remote Deployed Routees](http://developer.lightbend.com/guides/akka-sample-cluster-scala/#router-example-with-pool-of-remote-deployed-routees)
---------------------------------------------

http://doc.akka.io/docs/akka/2.5/scala/routing.html

a cluster aware router on single master node that creates and deploys workers
instead of looking them up.

config

```
router = cluster-metrics-adaptive-group
```

Event emitter
-------------

```bash
bash devops/event-emitter.sh 100 127.0.0.1 2555 127.0.0.1:2555
[INFO] [09/06/2017 01:41:29.516] [main] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2555] - Registered cluster JMX MBean [akka:type=Cluster]

[INFO] [09/06/2017 01:47:20.531] [ClusterSystem-akka.actor.default-dispatcher-15] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2555] - Leader is moving node [akka.tcp://ClusterSystem@127.0.0.1:2556] to [Exiting]
```

processor node 1

```
# consumer that receives max. 4 msg / [s],
# processing time 250 ms,
# hostname 127.0.0.1, port 2556,
# seednode 127.0.0.1:2555

bash devops/event-processor.sh 250 127.0.0.1 2556 127.0.0.1:2555
[INFO] [09/06/2017 01:46:19.243] [main] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2556] - Started up successfully


[ERROR] [SECURITY][09/06/2017 01:47:19.963] [ClusterSystem-akka.remote.default-remote-dispatcher-4] [akka.actor.ActorSystemImpl(ClusterSystem)] Uncaught error from thread [ClusterSystem-akka.remote.default-remote-dispatcher-4]: Java heap space, shutting down JVM since 'akka.jvm-exit-on-fatal-error' is enabled for ActorSystem[ClusterSystem]
java.lang.OutOfMemoryError: Java heap space
	at akka.protobuf.ByteString.copyFrom(ByteString.java:192)
	at akka.protobuf.CodedInputStream.readBytes(CodedInputStream.java:324)
	at akka.remote.ContainerFormats$SelectionEnvelope.<init>(ContainerFormats.java:231)
	at akka.remote.ContainerFormats$SelectionEnvelope.<init>(ContainerFormats.java:181)
	at akka.remote.ContainerFormats$SelectionEnvelope$1.parsePartialFrom(ContainerFormats.java:290)
	at akka.remote.ContainerFormats$SelectionEnvelope$1.parsePartialFrom(ContainerFormats.java:285)
	at akka.protobuf.AbstractParser.parsePartialFrom(AbstractParser.java:141)
	at akka.protobuf.AbstractParser.parseFrom(AbstractParser.java:177)
	at akka.protobuf.AbstractParser.parseFrom(AbstractParser.java:188)
	at akka.protobuf.AbstractParser.parseFrom(AbstractParser.java:193)
	at akka.protobuf.AbstractParser.parseFrom(AbstractParser.java:49)
	at akka.remote.ContainerFormats$SelectionEnvelope.parseFrom(ContainerFormats.java:510)
	at akka.remote.serialization.MessageContainerSerializer.fromBinary(MessageContainerSerializer.scala:70)
	at akka.serialization.Serialization.deserializeByteArray(Serialization.scala:157)
	at akka.serialization.Serialization.$anonfun$deserialize$2(Serialization.scala:143)
	at akka.serialization.Serialization$$Lambda$441/529926139.apply(Unknown Source)
	at scala.util.Try$.apply(Try.scala:209)
	at akka.serialization.Serialization.deserialize(Serialization.scala:137)
	at akka.remote.MessageSerializer$.deserialize(MessageSerializer.scala:33)
	at akka.remote.DefaultMessageDispatcher.payload$lzycompute$1(Endpoint.scala:64)
	at akka.remote.DefaultMessageDispatcher.payload$1(Endpoint.scala:64)
	at akka.remote.DefaultMessageDispatcher.dispatch(Endpoint.scala:82)
	at akka.remote.EndpointReader$$anonfun$receive$2.applyOrElse(Endpoint.scala:982)
	at akka.actor.Actor.aroundReceive(Actor.scala:513)
	at akka.actor.Actor.aroundReceive$(Actor.scala:511)
	at akka.remote.EndpointActor.aroundReceive(Endpoint.scala:446)
	at akka.actor.ActorCell.receiveMessage(ActorCell.scala:527)
	at akka.actor.ActorCell.invoke(ActorCell.scala:496)
	at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:257)
	at akka.dispatch.Mailbox.run(Mailbox.scala:224)
	at akka.dispatch.Mailbox.exec(Mailbox.scala:234)
	at akka.dispatch.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)

[INFO] [09/06/2017 01:47:30.478] [ClusterSystem-akka.remote.default-remote-dispatcher-13] [akka.tcp://ClusterSystem@127.0.0.1:2556/system/remoting-terminator] Remoting shut down.
```

processor node 2

```
# consumer that receives max. 4 msg / [s]

bash devops/event-processor.sh 250 127.0.0.1 2557 127.0.0.1:2555
[INFO] [09/06/2017 01:56:09.815] [ClusterSystem-akka.actor.default-dispatcher-16] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2557] - Metrics collection has started successfully

WARNING: An exception was thrown by a user handler while handling an exception event ([id: 0x62eef4a8, /127.0.0.1:50725 => /127.0.0.1:2557] EXCEPTION: java.lang.OutOfMemoryError: Java heap space)
java.lang.OutOfMemoryError: Java heap space
	at org.jboss.netty.buffer.HeapChannelBuffer.<init>(HeapChannelBuffer.java:42)
	at org.jboss.netty.buffer.BigEndianHeapChannelBuffer.<init>(BigEndianHeapChannelBuffer.java:34)
	at org.jboss.netty.buffer.ChannelBuffers.buffer(ChannelBuffers.java:134)
	at org.jboss.netty.buffer.HeapChannelBufferFactory.getBuffer(HeapChannelBufferFactory.java:68)
	at org.jboss.netty.buffer.AbstractChannelBufferFactory.getBuffer(AbstractChannelBufferFactory.java:48)
	at org.jboss.netty.handler.codec.frame.FrameDecoder.newCumulationBuffer(FrameDecoder.java:507)
	at org.jboss.netty.handler.codec.frame.FrameDecoder.updateCumulation(FrameDecoder.java:345)
	at org.jboss.netty.handler.codec.frame.FrameDecoder.messageReceived(FrameDecoder.java:312)
	at org.jboss.netty.channel.SimpleChannelUpstreamHandler.handleUpstream(SimpleChannelUpstreamHandler.java:70)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendUpstream(DefaultChannelPipeline.java:564)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendUpstream(DefaultChannelPipeline.java:559)
	at org.jboss.netty.channel.Channels.fireMessageReceived(Channels.java:268)
	at org.jboss.netty.channel.Channels.fireMessageReceived(Channels.java:255)
	at org.jboss.netty.channel.socket.nio.NioWorker.read(NioWorker.java:88)
	at org.jboss.netty.channel.socket.nio.AbstractNioWorker.process(AbstractNioWorker.java:108)
	at org.jboss.netty.channel.socket.nio.AbstractNioSelector.run(AbstractNioSelector.java:337)
	at org.jboss.netty.channel.socket.nio.AbstractNioWorker.run(AbstractNioWorker.java:89)
	at org.jboss.netty.channel.socket.nio.NioWorker.run(NioWorker.java:178)
	at org.jboss.netty.util.ThreadRenamingRunnable.run(ThreadRenamingRunnable.java:108)
	at org.jboss.netty.util.internal.DeadLockProofWorker$1.run(DeadLockProofWorker.java:42)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
```

processor node 3

```
# processor that receives max. 2 msg / [s]

bash devops/event-processor.sh 500 127.0.0.1 2558 127.0.0.1:2555
[INFO] [09/06/2017 01:59:39.940] [ClusterSystem-akka.actor.default-dispatcher-3] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2558] - Metrics collection has started successfully

```


processor node 4

```
# consumer that receives max. 1 msg / [s]

bash devops/event-processor.sh 1000 127.0.0.1 2559 127.0.0.1:2555
[INFO] [09/06/2017 02:02:22.275] [ClusterSystem-akka.actor.default-dispatcher-2] [akka.cluster.Cluster(akka://ClusterSystem)] Cluster Node [akka.tcp://ClusterSystem@127.0.0.1:2559] - Metrics collection has started successfully
```