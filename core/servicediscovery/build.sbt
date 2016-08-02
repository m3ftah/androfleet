
lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging).
  settings(
    name := "androfleet-servicediscovery",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies := Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.7",
      "com.typesafe.akka" %% "akka-remote" % "2.4.7",
      "com.typesafe.play" %% "play-json" % "2.5.4",
      "commons-net" % "commons-net" % "3.5"
    )
  )
