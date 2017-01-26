package datameer.configuration

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConfigFilesSuite extends FunSuite {

  test("List property files only") {
    val resourceFolder = getClass.getClassLoader.getResource("ConfigFilesSuite/list-property-files").getFile
    assert(ConfigFiles.propertyFiles(new File(resourceFolder)).map(f => f.getName).toList === List("foo.properties", "bar.properties"))
  }

  test("Yield property files with same name") {
    val root1 = getClass.getClassLoader.getResource("ConfigFilesSuite/yield-property-files/root1").getFile
    val root2 = getClass.getClassLoader.getResource("ConfigFilesSuite/yield-property-files/root2").getFile
    assert(ConfigFiles
      .yieldPropertyFilesWithSameName(new File(root1), new File(root2))
      .map((tuple: (File, File)) => (tuple._1.getName, tuple._2.getName))
      .toList === List(("bar.properties", "bar.properties"), ("foo.properties", "foo.properties")))
  }

  test("To case class PropertyFile") {
    val root1 = getClass.getClassLoader.getResource("ConfigFilesSuite/yield-property-files/root1").getFile
    val root2 = getClass.getClassLoader.getResource("ConfigFilesSuite/yield-property-files/root2").getFile
    // assert file name
    assert(ConfigFiles
      .yieldPropertyFilesWithSameName(new File(root1), new File(root2))
      .map(ConfigFiles.toPropertyFile)
      .map(t => (t._1.file.getName, t._2.file.getName))
      .toList === List(("bar.properties", "bar.properties"), ("foo.properties", "foo.properties")))

    // assert file1
    assert(ConfigFiles
      .yieldPropertyFilesWithSameName(new File(root1), new File(root2))
      .map(ConfigFiles.toPropertyFile)
      .map(t => (t._1.properties.keys))
      .toList === List(Set("root1.bar"), Set("root1.foo")))

    // assert file2
    assert(ConfigFiles
      .yieldPropertyFilesWithSameName(new File(root1), new File(root2))
      .map(ConfigFiles.toPropertyFile)
      .map(t => (t._2.properties.keys))
      .toList === List(Set("root2.bar"), Set("root2.foo")))
  }

}

