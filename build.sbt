
githubOwner := "jdaviderb"
githubRepository := "ditto-circle"
name := "ditto-circle"

version := "0.5"
externalResolvers += "DittoSerializer" at "https://maven.pkg.github.com/jdaviderb/ditto-serializer"

publishMavenStyle := true
coverageEnabled := true
coverageHighlighting := true

val circeVersion = "0.12.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "jdaviderb" %% "ditto-serializer" % "0.5",
  "io.circe" %% "circe-parser" % circeVersion % "test",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

scalaVersion := "2.13.1"
