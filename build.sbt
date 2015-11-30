name := "orion"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.github.scopt" %% "scopt" % "3.2.0",

  // -- config --
  "com.typesafe" % "config" % "1.2.0",

  // -- Http clients
  "org.scalaj" %% "scalaj-http" % "2.0.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
)

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "io.spray"            %%  "spray-client"  % sprayV,
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test"
  )
}