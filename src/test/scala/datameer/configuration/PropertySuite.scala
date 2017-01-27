package datameer.configuration

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PropertySuite extends FunSuite {

  test("Property is empty ") {
    val pe: Property = Empty
    assert(pe.isEmpty == true)
  }

  test("property is not empty after add") {
    val pe: Property = Empty.add("key", "value")
    assert(pe.isEmpty == false)
  }

  test("empty property throws exc when calling key") {
    val pe: Property = Empty
    intercept[NoSuchElementException] {
      pe.head
    }
  }

  test("retrieve key/value from the property") {
    val pe: Property = Empty.add("key", "value")
    assert(pe.head._1 == "key")
    assert(pe.head._2 == "value")
  }

  test("tail of empty powerline throws exc") {
    intercept[NoSuchElementException] {
      Empty.tail
    }
  }

  test("head and tail combination of non empty powerline") {
    assert(Empty.add("key1", "value1").add("key2", "value2").tail.head._1 == "key2")
    assert(Empty.add("key1", "value1").add("key2", "value2").tail.head._2 == "value2")
  }

  test("keys") {
    val properties = Empty.add("key1", "value1").add("key2", "value2").add("key3", "value3")
    assert(properties.keys === Set("key1", "key2", "key3"))
  }

  test("get property") {
    val properties = Empty.add("key1", "value1").add("key2", "value2").add("key3", "value3")
    assert(properties.getValue("key1") === Some("value1"))
    assert(properties.getValue("key2") === Some("value2"))
    assert(properties.getValue("key3") === Some("value3"))
    assert(properties.getValue("keyNotExists") === None)
  }
}