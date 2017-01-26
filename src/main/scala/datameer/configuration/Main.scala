package datameer.configuration

import java.io.File

import datameer.configuration.ConfigFiles.PropertyFile

import scala.io.StdIn

object Main {


  val HEADER_FORMAT = "%-50s %-80s %-80s %n";

  def main(args: Array[String]): Unit = {
    print("Path to the new Datameer Installation: ")
    val newDatameer = StdIn.readLine()

    print("Path to the old Datameer Installation: ")
    val oldDatameer = StdIn.readLine()

    val propertyFiles = ConfigFiles
      .yieldPropertyFilesWithSameName(new File(oldDatameer + "/conf"), new File(newDatameer + "/conf"))
      .map(ConfigFiles.toPropertyFile)


    printUpdatedEntries(propertyFiles)
    printMissingEntries(propertyFiles)
    printMissingEntries(propertyFiles.map((tuple: (PropertyFile, PropertyFile)) => (tuple._2, tuple._1)))

  }

  private def printUpdatedEntries(propertyFiles: Stream[(PropertyFile, PropertyFile)]) = {
    propertyFiles
      .foreach((tuple: (PropertyFile, PropertyFile)) => {
        val tuples = for {
          key1 <- tuple._1.properties.keys
          value1 <- tuple._1.properties.getValue(key1)
          value2 <- tuple._2.properties.getValue(key1)
          if (value1 != value2)
        } yield (key1, value1, value2)
        if (!tuples.isEmpty) {
          println()
          print(String.format(HEADER_FORMAT, "key", tuple._1.file, tuple._2.file))
          tuples.foreach((t: (String, String, String)) => print(String.format(HEADER_FORMAT, t._1, t._2, t._3)))
        }
      })
  }

  private def printMissingEntries(propertyFiles: Stream[(PropertyFile, PropertyFile)]) = {
    propertyFiles
      .foreach((tuple: (PropertyFile, PropertyFile)) => {
        val tuples = for {
          key1 <- tuple._1.properties.keys
          value1 <- tuple._1.properties.getValue(key1)
          if (tuple._2.properties.getValue(key1).isEmpty)
        } yield (key1, value1, "x")
        if (!tuples.isEmpty) {
          println()
          print(String.format(HEADER_FORMAT, "key", tuple._1.file, tuple._2.file))
          tuples.foreach((t: (String, String, String)) => print(String.format(HEADER_FORMAT, t._1, t._2, t._3)))
        }
      })
  }

}
