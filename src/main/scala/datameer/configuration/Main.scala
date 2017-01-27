package datameer.configuration

import java.io.File

import datameer.configuration.ConfigFiles.PropertyFile

import scala.io.StdIn

object Main {

  val HEADER_FORMAT = "%-50s %-80s %-80s %n"

  def main(args: Array[String]): Unit = {
    print("Path to the new Datameer Installation: ")
    val newDatameer = StdIn.readLine()

    print("Path to the old Datameer Installation: ")
    val oldDatameer = StdIn.readLine()

    val propertyFiles = ConfigFiles
      .yieldPropertyFilesWithSameName(new File(oldDatameer + "/conf"), new File(newDatameer + "/conf"))
      .map(ConfigFiles.toPropertyFile)

    val printf = (t: (String, String, String)) => print(String.format(HEADER_FORMAT, t._1, t._2, t._3))
    val printf2 = (t: (String, String, String)) => print(String.format(HEADER_FORMAT, t._1, t._3, t._2))

    printUpdatedEntries(propertyFiles, printf)
    printMissingEntries(propertyFiles, printf)
    printMissingEntries(propertyFiles.map((tuple: (PropertyFile, PropertyFile)) => tuple.swap), printf2)
  }


  private def printUpdatedEntries(propertyFiles: Stream[(PropertyFile, PropertyFile)], printf: ((String, String, String)) => Unit) = {
    propertyFiles
      .foreach((tuple: (PropertyFile, PropertyFile)) => {
        val tuples = for {
          key1 <- tuple._1.properties.keys
          value1 <- tuple._1.properties.getValue(key1)
          value2 <- tuple._2.properties.getValue(key1)
          if value1 != value2
        } yield (key1, value1, value2)
        if (tuples.nonEmpty) {
          println()
          println()
          printf(("PropertyKey", tuple._1.file.getPath, tuple._2.file.getPath))
          tuples.foreach(printf)
        }
      })
  }

  private def printMissingEntries(propertyFiles: Stream[(PropertyFile, PropertyFile)], printf: ((String, String, String)) => Unit) = {
    propertyFiles
      .foreach((tuple: (PropertyFile, PropertyFile)) => {
        val tuples = for {
          key1 <- tuple._1.properties.keys
          value1 <- tuple._1.properties.getValue(key1)
          if tuple._2.properties.getValue(key1).isEmpty
        } yield (key1, value1, "x")
        if (tuples.nonEmpty) {
          println()
          tuples.foreach((tuple: (String, String, String)) => tuple.copy(_1 = tuple._2))
          printf(("PropertyKey", tuple._1.file.getPath, tuple._2.file.getPath))
          tuples.foreach(printf)
        }
      })
  }
}
