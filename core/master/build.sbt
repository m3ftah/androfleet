
lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging).
  settings(
    name := "androfleet-master",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies := Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.7",
      "com.typesafe.akka" %% "akka-remote" % "2.4.7"
    )
  )
