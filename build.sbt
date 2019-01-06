organization in ThisBuild := "org.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagomdemo` = (project in file("."))
  .aggregate(`lagomdemo-api`, `lagomdemo-impl`, `lagomdemo-stream-api`, `lagomdemo-stream-impl`)

lazy val `lagomdemo-api` = (project in file("lagomdemo-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagomdemo-impl` = (project in file("lagomdemo-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`lagomdemo-api`)

lazy val `lagomdemo-stream-api` = (project in file("lagomdemo-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagomdemo-stream-impl` = (project in file("lagomdemo-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagomdemo-stream-api`, `lagomdemo-api`)
