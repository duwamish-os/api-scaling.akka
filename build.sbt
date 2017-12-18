name := "api.scaling.akka"

version := "1.0"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.8"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies += "org.junit" % "junit5-engine" % "5.0.0-ALPHA"

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.0.2" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % "test"
