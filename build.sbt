
// This file is a result of a partial attempt at switching to sbt from pom.xml (because it supposedly generates docs for scala code).
// Source instrctions: http://www.scala-sbt.org/1.0/docs/Using-Sonatype.html . Not completed.

name := "db-interface"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

val scalactestVersion = "3.0.5"
val logbackVersion = "1.2.3"
val json4sVersion = "3.6.1"


libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % logbackVersion
  ,"ch.qos.logback" % "logback-core" % logbackVersion
  ,"org.json4s" % "json4s-ast_2.12" % json4sVersion
  ,"org.json4s" % "json4s-native_2.12" % json4sVersion
  ,"org.scala-lang.modules" %% "scala-xml" % "1.1.1"
  ,"org.apache.commons" % "commons-csv" % "1.5"
  ,"com.github.sanskrit-coders" % "indic-transliteration_2.12" % "1.30"
  //    ,"com.github.sanskrit-coders" % "sanskrit-lttoolbox" % "0.1"
)

libraryDependencies += "org.scalactic" %% "scalactic" % scalactestVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalactestVersion % "test"

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
