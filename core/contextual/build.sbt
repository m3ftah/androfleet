
lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging).
  settings(
    name := "androfleet-contextual",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies := Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.7",
      "com.typesafe.akka" %% "akka-remote" % "2.4.7",
      "io.spray" %% "spray-routing" % "1.3.3",
      "io.spray" %% "spray-can" % "1.3.3",
      "org.json4s" %% "json4s-jackson" % "3.4.0"
    )
  )
