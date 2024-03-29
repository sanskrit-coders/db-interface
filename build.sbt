
// Source instrctions: http://www.scala-sbt.org/1.0/docs/Using-Sonatype.html . 

name := "db-interface"

scalaVersion := "2.13.10"
val logbackVersion = "1.2.3"
val json4sVersion = "4.0.6"
val scalatestVersion = "3.2.14"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"



libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % logbackVersion
  , "ch.qos.logback" % "logback-core" % logbackVersion
  , "org.json4s" % "json4s-ast_2.13" % json4sVersion
  , "org.json4s" % "json4s-native_2.13" % json4sVersion
  ,"org.scala-lang.modules" %% "scala-xml" % "1.1.1"
  ,"org.apache.commons" % "commons-csv" % "1.5"
  , "com.github.scopt" % "scopt_2.13" % "4.1.0"
  ,"com.github.sanskrit-coders" % "indic-transliteration_2.13" % "1.30"
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


assemblyOutputPath in assembly := file("bin/artifacts/db-interface.jar")
mainClass in assembly := Some("dbUtils.commandInterface")

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  releaseStepCommand("assembly"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
