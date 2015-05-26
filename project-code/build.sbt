name := "geolocation"

version := "2.0.0"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.4", "2.11.5")

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaCore, javaWs, cache
)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)

organization := "com.edulify"

organizationName := "Edulify.com"

organizationHomepage := Some(new URL("https://edulify.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

startYear := Some(2013)

description := "This is a geolocation library for Playframework."

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("http://edulify.github.io/play-geolocation-module.edulify.com/"))

pomExtra := (
  <scm>
    <url>https://github.com/edulify/play-geolocation-module.edulify.com</url>
    <connection>scm:git:git@github.com:edulify/play-geolocation-module.edulify.com.git</connection>
    <developerConnection>scm:git:https://github.com/edulify/play-geolocation-module.edulify.com.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>megazord</id>
        <name>Megazord</name>
        <email>contact [at] edulify.com</email>
        <url>https://github.com/megazord</url>
      </developer>
      <developer>
        <id>ranierivalenca</id>
        <name>Ranieri Valença</name>
        <email>ranierivalenca [at] edulify.com</email>
        <url>https://github.com/ranierivalenca</url>
      </developer>
    </developers>
  )

scalacOptions := Seq("-feature", "-deprecation")
