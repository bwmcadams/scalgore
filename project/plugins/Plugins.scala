import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  lazy val akkaRepo = "Akka Repository" at "http://akka.io/repository"
  
  lazy val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0"
}