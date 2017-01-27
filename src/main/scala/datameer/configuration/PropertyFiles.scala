package datameer.configuration

import java.io.{File, FileInputStream}

import scala.collection.JavaConverters._

object PropertyFiles {

  def propertyFiles(root: File, skipHidden: Boolean = false): Stream[File] = {
    tree(root).filter(f => f.isFile && f.getName.endsWith("properties"))
  }

  def yieldPropertyFilesWithSameName(root1: File, root2: File): Stream[(File, File)] = {
    return for {
      p1 <- propertyFiles(root1);
      p2 <- propertyFiles(root2);
      if (p1.getName == p2.getName)
    } yield (p1, p2)
  }

  def toPropertyFile(tuple: (File, File)): (PropertyFile, PropertyFile) = {
    tupleLift(loadProperties, tuple)
  }

  private def tupleLift[A, B](f: A => B, tuple: (A, A)): (B, B) = {
    (f(tuple._1), f(tuple._2))
  }

  private def loadProperties(file: File): PropertyFile = {
    val p = new java.util.Properties()
    p.load(new FileInputStream(file))
    val z: Property = Empty
    val property = p.asScala.foldLeft(z)((acc: Property, tuple: (String, String)) => acc.add(tuple._1, tuple._2))
    PropertyFile(file, Properties(property))
  }

  private def tree(root: File, skipHidden: Boolean = false): Stream[File] = {
    if (!root.exists || (skipHidden && root.isHidden)) Stream.empty
    else root #:: (
      root.listFiles match {
        case null => Stream.empty
        case files => files.toStream.flatMap(tree(_, skipHidden))
      })
  }

  case class PropertyFile(file: File, properties: Properties)

}