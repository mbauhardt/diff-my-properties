package datameer.configuration

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PropertiesSuite extends FunSuite {

  test("Properties is empty on an empty powerline element") {
    val pe: Property = Empty
    assert(Properties(pe).isEmpty)
  }

  test("Properties is non empty on an empty powerline element") {
    val pe: Property = Empty.add("key", "value")
    assert(!Properties(pe).isEmpty)
  }

  test("head of empty powerline throws exc") {
    intercept[NoSuchElementException] {
      Properties(Empty).head
    }
  }

  test("tail of empty powerline throws exc") {
    intercept[NoSuchElementException] {
      Properties(Empty).tail
    }
  }

  test("head of non empty powerline ") {
    assert(Properties(Empty.add("key", "value")).head.key == "key")
    assert(Properties(Empty.add("key", "value")).head.value == "value")
  }

  test("head and tail combination of non empty powerline") {
    assert(Properties(Empty.add("key1", "value1").add("key2", "value2")).tail.head.key == "key2")
    assert(Properties(Empty.add("key1", "value1").add("key2", "value2")).tail.head.value == "value2")
  }

  test("keys") {
    val properties = Properties(Empty.add("key1", "value1").add("key2", "value2").add("key3", "value3"))
    assert(properties.keys === Set("key1", "key2", "key3"))
  }

  test("get property") {
    val properties = Properties(Empty.add("key1", "value1").add("key2", "value2").add("key3", "value3"))
    assert(properties.getValue("key1") === Some("value1"))
    assert(properties.getValue("key2") === Some("value2"))
    assert(properties.getValue("key3") === Some("value3"))
    assert(properties.getValue("keyNotExists") === None)
  }
}