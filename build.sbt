name := "api.scaling.akka"

version := "1.0"

scalaVersion := "2.12.3"

val akkaVersion = "2.5.3"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion