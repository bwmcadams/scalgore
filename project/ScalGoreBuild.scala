import sbt._
import Keys._

object ScalGoreBuild extends Build {
  import Dependencies._
  import Resolvers._
  import Publish._
  
  //lazy val mainClass = Some("net.evilmonkeylabs.scalgore.ScalGore")

  lazy val buildSettings = Seq(
    organization := "net.evilmonkeylabs",
    version      := "1.0-SNAPSHOT",
    scalaVersion := "2.9.0-1",
    mainClass    := Some("net.evilmonkeylabs.scalgore.ScalGore")
  )

  override lazy val settings = super.settings ++ buildSettings

  lazy val baseSettings = Defaults.defaultSettings 

  lazy val parentSettings = baseSettings 

  lazy val defaultSettings = baseSettings ++ Seq(
    libraryDependencies ++= Seq(specs2, slf4jJCL),
    resolvers ++= Seq(scalaToolsReleases, akkaRepo, scalaToolsSnapshots, mavenOrgRepo, twttrRepo, sonatypeReleasesRepo),
    autoCompilerPlugins := true,
    parallelExecution in Test := true,
    testFrameworks += TestFrameworks.Specs2
  )

  lazy val scalgore = Project(
    id        = "scalgore",
    base      = file("."),
    settings  = parentSettings,
    aggregate = Seq(bot, web)
  )

  lazy val bot = Project(
    id       = "scalgore-bot",
    base     = file("scalgore-bot"),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Seq(elasticSearch, commonsHTTP, casbah, akka_actor, akka_camel, camel_irc, configgy, specs2, slf4jJCL)
    )
  )

  lazy val web = Project(
    id       = "scalgore-web",
    base     = file("scalgore-web"),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Seq()
    )
  )

}

object Dependencies {

  val elasticSearch = "org.elasticsearch" % "elasticsearch" % "0.16.3"
  val commonsHTTP = "commons-httpclient" % "commons-httpclient" % "3.1"
  
  val casbah = "com.mongodb.casbah" %% "casbah" % "2.1.5-1"
  val akka_actor = "se.scalablesolutions.akka" % "akka-actor" % "1.1.3"
  val akka_camel = "se.scalablesolutions.akka" % "akka-camel" % "1.1.3"
  val camel_irc = "org.apache.camel" % "camel-irc" % "2.5.0"
  val configgy = "net.lag" % "configgy" % "2.0.2"

  val specs2 = "org.specs2" %% "specs2" % "1.5" % "test"
  val specs2Compile = "org.specs2" %% "specs2" % "1.5"

  // JCL bindings for testing only
  val slf4jJCL         = "org.slf4j" % "slf4j-jcl" % "1.6.0" % "test"

}

object Resolvers {
  val scalaToolsSnapshots = "snapshots" at "http://scala-tools.org/repo-snapshots"
  val scalaToolsReleases  = "releases" at "http://scala-tools.org/repo-releases"

  val akkaRepo = "Akka Repository" at "http://repo.typesafe.com/typesafe/akka-repo"
  val jbossRepo = "JBoss Public Repo" at "https://repository.jboss.org/nexus/content/groups/public-jboss/"
  val mavenOrgRepo = "Maven.Org Repository" at "http://repo1.maven.org/maven2/org/"
  val twttrRepo = "Twitter Public Repo" at "http://maven.twttr.com"
  val sonatypeReleasesRepo = "Sonatype OSS Repo" at "http://oss.sonatype.org/content/repositories/releases"
}


object Publish {
  lazy val settings = Seq(
    publishTo <<= version(v => Some(publishTarget(v))),
    credentials += Credentials(Path.userHome / ".ivy2" / ".scalatools_credentials")
  )

  private def publishTarget(version: String) = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/%s/".format(
    if (version.endsWith("-SNAPSHOT"))
      "snapshots"
    else
      "releases"
  )
}


// vim: set ts=2 sw=2 sts=2 et:
