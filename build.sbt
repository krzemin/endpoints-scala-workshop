
name := "endpoints-scala-workshop"

version := "1.0.0"

scalaVersion := "2.12.8"

val endpointsVersion = "0.8.0"
val akkaVersion = "2.5.19"

libraryDependencies ++= Seq(
  "org.julienrf" %% "endpoints-algebra" % endpointsVersion,
  "org.julienrf" %% "endpoints-openapi" % endpointsVersion,
  "org.julienrf" %% "endpoints-json-schema-generic" % endpointsVersion,
  "org.julienrf" %% "endpoints-akka-http-server" % endpointsVersion,
  "org.julienrf" %% "endpoints-sttp-client" % endpointsVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.7",
  "de.heikoseeberger" %% "akka-http-circe" % "1.23.0",
  "com.softwaremill.sttp" %% "core" % "1.5.4"
)

cancelable in Global := true

fork in run := true
