name := "api.scaling.akka"

version := "1.0"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.8"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP9" % Test
