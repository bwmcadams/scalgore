import sbt._

class ScalGoreProject(info: ProjectInfo) extends DefaultProject(info) {
  override val mainClass = Some("net.evilmonkeylabs.scalgore.ScalGore")

  val elasticSearch = "org.elasticsearch" % "elasticsearch" % "0.15.0"
  val commonsHTTP = "commons-httpclient" % "commons-httpclient" % "3.1"
  
  val casbah = "com.mongodb.casbah" %% "casbah" % "2.0.3"
  val akka_actor = "se.scalablesolutions.akka" % "akka-actor" % "1.0"
  val configgy = "net.lag" % "configgy" % "2.0.2"
  
  val twitterRep = "Twitter" at "http://maven.twttr.com"
  val sonaRepo = "Sona Repo" at "http://oss.sonatype.org/content/repositories/releases/"
  val akkaRepo = "Akka Repository" at "http://www.akka.io/repository"
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
}

// vim: set ts=2 sw=2 sts=2 et:
