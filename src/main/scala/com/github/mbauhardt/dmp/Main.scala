package com.github.mbauhardt.dmp

import java.io.File

import com.github.mbauhardt.dmp.PropertyFiles.PropertyFile
import com.lexicalscope.jewel.cli.CliFactory


object Main {

  val HEADER_FORMAT = "%-70s %-80s %-80s %n"


  def main(args: Array[String]): Unit = {

    var options: Options = null;
    try {
      //val array = JavaConversions.asJavaCollection(args).
      options = CliFactory.parseArguments(classOf[Options], args: _*);
    } catch {
      case e: Exception => println(e.getMessage)
        System.exit(-1);
    }

    val rightFolder = options.getRightFolder
    val leftFolder = options.getLeftFolder
    val propertyFiles = PropertyFiles
      .yieldPropertyFilesWithSameName(new File(leftFolder), new File(rightFolder))
      .map(PropertyFiles.toPropertyFile)

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
        } yield (key1, value1, "-")
        if (tuples.nonEmpty) {
          println()
          tuples.foreach((tuple: (String, String, String)) => tuple.copy(_1 = tuple._2))
          printf(("PropertyKey", tuple._1.file.getPath, tuple._2.file.getPath))
          tuples.foreach(printf)
        }
      })
  }
}
