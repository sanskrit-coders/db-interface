
// This file is a result of a partial attempt at switching to sbt from pom.xml (because it supposedly generates docs for scala code).
// Source instrctions: http://www.scala-sbt.org/1.0/docs/Using-Sonatype.html . Not completed.

name := "db-interface"
version := "2.9"

scalaVersion := "2.12.3"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "0.9.29"
  ,"ch.qos.logback" % "logback-core" % "0.9.29"
  ,"net.ruippeixotog" % "scala-scraper_2.11" % "1.2.1"
  ,"org.scala-lang" % "scala-xml" % "2.11.0-M4"
  ,"org.json4s" % "json4s_2.11" % "3.2.11"
  ,"org.json4s" % "json4s-native_2.11" % "3.2.11"
  ,"org.apache.commons" % "commons-csv" % "1.4"
  ,"com.github.sanskrit-coders" % "indic-transliteration" % "1.6"
  //    ,"com.github.sanskrit-coders" % "sanskrit-lttoolbox" % "0.1"
  //  ,"com.github.sanskrit-coders" % "db-interface" % "1.8"
)

//unmanagedJars in (Compile, run) += file("/home/vvasuki/db-interface/target/db-interface-1.8.jar")
//unmanagedClasspath in (Compile, run) += file("/home/vvasuki/db-interface/target/db-interface-1.8/classes")
//unmanagedSources in (Compile, run)  += file("/home/vvasuki/db-interface/src/main/scala")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sanskrit-coders/db-interface"),
    "scm:git@github.com:sanskrit-coders/db-interface.git"
  )
)

useGpg := true
publishMavenStyle := true
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
