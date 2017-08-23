
name          := "local-imports"
organization  := "ohnosequences"
description   := "A compiler plugin providing syntax for local imports"
bucketSuffix  := "era7.com"
scalaVersion  := "2.12.3"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest"      % "3.0.1"             % "test"
)

dependencyOverrides ++= Set(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)

scalacOptions in console in Compile <+= (packageBin in Compile) map {
  pluginJar => "-Xplugin:" + pluginJar
}

scalacOptions in Test <+= (packageBin in Compile) map {
  pluginJar => "-Xplugin:" + pluginJar
}
