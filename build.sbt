
name := "endpoints-scala-workshop"

version := "1.0.0"

scalaVersion := "2.12.8"

val endpointsVersion = "0.10.1"
val akkaVersion = "2.5.25"

libraryDependencies ++= Seq(
  "org.julienrf" %% "endpoints-algebra" % endpointsVersion,
  "org.julienrf" %% "endpoints-openapi" % endpointsVersion,
  "org.julienrf" %% "endpoints-json-schema-generic" % endpointsVersion,
  "org.julienrf" %% "endpoints-json-schema-circe" % endpointsVersion,
  "org.julienrf" %% "endpoints-akka-http-server" % endpointsVersion,
  "org.julienrf" %% "endpoints-akka-http-server-circe" % endpointsVersion,
  "org.julienrf" %% "endpoints-sttp-client" % endpointsVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.9",
  "de.heikoseeberger" %% "akka-http-circe" % "1.27.0",
  "com.softwaremill.sttp" %% "core" % "1.6.6"
)

cancelable in Global := true

fork in run := true
