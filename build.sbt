addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

name := "db-interface"

version := "2.5"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "0.9.29"
  ,"ch.qos.logback" % "logback-core" % "0.9.29"
  ,"net.ruippeixotog" % "scala-scraper_2.11" % "1.2.1"
  ,"org.scala-lang" % "scala-xml" % "2.11.0-M4"
  ,"org.json4s" % "json4s_2.11" % "3.2.11"
  ,"org.json4s" % "json4s-native_2.11" % "3.2.11"
  ,"org.apache.commons" % "commons-csv" % "1.4"
  ,"com.github.sanskrit-coders" % "indic-transliteration" % "1.1"
  //    ,"com.github.sanskrit-coders" % "sanskrit-lttoolbox" % "0.1"
  //  ,"com.github.sanskrit-coders" % "db-interface" % "1.8"
)

//unmanagedJars in (Compile, run) += file("/home/vvasuki/db-interface/target/db-interface-1.8.jar")
//unmanagedClasspath in (Compile, run) += file("/home/vvasuki/db-interface/target/db-interface-1.8/classes")
//unmanagedSources in (Compile, run)  += file("/home/vvasuki/db-interface/src/main/scala")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

homepage := Some(url("http://example.com"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sanskrit-coders/db-interface"),
    "scm:git@github.com:sanskrit-coders/db-interface.git"
  )
)

developers := List(
  Developer(
    id    = "sanskrit-coders",
    name  = "Sanskrit Coders",
    email = "sanskrit-programmers@googlegroups.com",
    url   = url("https://sites.google.com/site/sanskritcode/")
  )
)

useGpg := true
publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}