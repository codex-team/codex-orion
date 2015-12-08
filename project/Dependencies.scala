import sbt._

object Dependencies {

  val resolutionRepos = Seq(
    "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"
  )

  val akkaVersion       = "2.4.0"
  val slf4jVersion      = "1.7.7"

  // Akka
  val akkaActor         = "com.typesafe.akka"           %%  "akka-actor"            % akkaVersion
  val akkaSlf4j         = "com.typesafe.akka"           %%  "akka-slf4j"            % akkaVersion
  val akkaTestKit       = "com.typesafe.akka"           %%  "akka-testkit"          % akkaVersion
  val akkaCluster       = "com.typesafe.akka"           %%  "akka-cluster"          % akkaVersion
  val akkaStream        = "com.typesafe.akka"           %%  "akka-stream-experimental" % "2.0-M1"


  val config            = "com.typesafe"                %  "config"                 % "1.2.0"
  val scopt             = "com.github.scopt"            %% "scopt"                  % "3.2.0"

  val scalajHttp        = "org.scalaj"                  %% "scalaj-http"            % "2.0.0"
  val scalaXml          = "org.scala-lang.modules"      %% "scala-xml"              % "1.0.2"
  val jsoup             = "org.jsoup"                   %  "jsoup"                  % "1.8.3"

  // Logging
  val scalaLogging      = "com.typesafe.scala-logging"  %%  "scala-logging"         % "3.1.0"
  val logback           = "ch.qos.logback"              %   "logback-classic"       % "1.0.13"
  val slf4jApi          = "org.slf4j"                   %   "slf4j-api"             % slf4jVersion
  val slf4jnop          = "org.slf4j"                   %   "slf4j-nop"             % slf4jVersion
  val slf4jJul          = "org.slf4j"                   %   "jul-to-slf4j"          % slf4jVersion
  val slf4jLog4j        = "org.slf4j"                   %   "log4j-over-slf4j"      % slf4jVersion

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")
  def optional  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile,optional")
}