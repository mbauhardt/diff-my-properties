name := "datameer-configuration"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation")

// grading libraries
libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
libraryDependencies += "junit" % "junit" % "4.10" % "test"
