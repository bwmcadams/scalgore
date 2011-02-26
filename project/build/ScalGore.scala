import sbt._

class ScalGoreProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  override val mainClass = Some("net.evilmonkeylabs.scalgore.ScalGore")

  val TwitterRepo = MavenRepository("Twitter Repository", "http://maven.twttr.com")
  val MavenLocal = MavenRepository("Local Maven Repository", "file://" + Path.userHome + "/.m2/repository")
  val SonatypeReleasesRepo = MavenRepository("Sonatype OSS Repo", "http://oss.sonatype.org/content/repositories/releases")


  val configgyConfig = ModuleConfiguration("net.lag.configgy", TwitterRepo)
  val elasticConfig = ModuleConfiguration("org.elasticsearch", SonatypeReleasesRepo)


  val elasticSearch = "org.elasticsearch" % "elasticsearch" % "0.15.0"
  val commonsHTTP = "commons-httpclient" % "commons-httpclient" % "3.1"

  // we already have akka-actor from AkkaProject
  val akka_camel = akkaModule("camel")

  val configgy = "net.lag" % "configgy" % "2.0.2"
  val casbah = "com.mongodb.casbah" %% "casbah" % "2.0.3"

}

// vim: set ts=2 sw=2 sts=2 et:
