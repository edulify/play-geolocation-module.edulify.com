import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "geolocation"
  val appVersion      = "1.0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    publishArtifact in(Compile, packageDoc) := false,
    organization := "com.edulify"
  )

}
