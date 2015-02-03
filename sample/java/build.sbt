name := "geolocation-java-sample"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  javaCore,
  javaJdbc,
  javaEbean,
  "com.edulify" %% "geolocation" % "1.3.0"
)

resolvers ++= Seq(
  Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
)
