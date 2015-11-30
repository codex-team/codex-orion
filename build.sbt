//import NativePackagerHelper._

name := "orion"

//orgranization := "codex.xyz"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

//enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.github.scopt" %% "scopt" % "3.2.0",
  // -- config --
  "com.typesafe" % "config" % "1.2.0",
  "org.scalaj" %% "scalaj-http" % "2.0.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
)

//// These options will be used for *all* versions.
//scalacOptions ++= Seq(
//  "-deprecation"
//  ,"-unchecked"
//  ,"-encoding", "UTF-8"
//  ,"-Xlint"
//  ,"-Yclosure-elim"
//  ,"-Yinline"
//  ,"-Xverify"
//  ,"-feature"
//  ,"-language:postfixOps"
//)
//
//mainClass in Compile := Some("xyz.codex.orion.Main")
//
//mappings in Universal ++= {
//  // optional example illustrating how to copy additional directory
//  directory("scripts") ++
//    // copy configuration files to config directory
//    contentOf("src/main/resources").toMap.mapValues("config/" + _)
//}
//
//// add 'config' directory first in the classpath of the start script,
//// an alternative is to set the config file locations via CLI parameters
//// when starting the application
//scriptClasspath := Seq("../config/") ++ scriptClasspath.value


// TODO move to the scala configuration