name := "geolocation-java-sample"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  javaCore,
  "com.edulify" %% "geolocation" % "2.1.0"
)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases")
)
