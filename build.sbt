

name := "diff-my-properties"

scalaVersion := "2.11.0"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint")

scalacOptions += "-target:jvm-1.7"

oneJarSettings

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "com.lexicalscope.jewelcli" % "jewelcli" % "0.8.9"