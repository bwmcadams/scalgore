import sbt._

class ScalGoreProject(info: ProjectInfo) extends DefaultProject(info) {
  override val mainClass = Some("net.evilmonkeylabs.scalgore.ScalGore")
  val akka = "se.scalablesolutions.akka" % "akka-core_2.8.0.RC3" % "0.9"
  val elasticSearch = "org.elasticsearch" % "elasticsearch" % "0.8.0"
  val commonsHTTP = "commons-httpclient" % "commons-httpclient" % "3.1"
  val akkaPersistence  = "se.scalablesolutions.akka" % "akka-persistence_2.8.0.RC3" % "0.9"
  val scalaToolsRepo = ScalaToolsReleases
  val scalaToolsSnaps = ScalaToolsSnapshots
  val sonaRepo = "Sona Repo" at "http://oss.sonatype.org/content/repositories/releases/"
  val sunRepo = "Sun Repository" at "http://download.java.net/maven/2/"
  val akkaRepo = "Akka Repository" at "http://www.scalablesolutions.se/akka/repository"
  val guiceRepo = "Guice Repo" at "http://guiceyfruit.googlecode.com/svn/repo/releases"
  val ibiblio = "Ibiblio Maven" at "http://mirrors.ibiblio.org/pub/mirrors/maven2"
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  //val thirdPartyMaven = "Local Third Party Maven Repository (Per Project)" at "file://   " + path("third-party.mvn").absString 
  //val jbossRepo = "JBoss Repo" at "http://repository.jboss.org/nexus/content/groups/public"
}

// vim: set ts=2 sw=2 sts=2 et:
