package datameer.configuration

import org.scalatest.FunSuite


class PropertiesSuite extends FunSuite {

  test("Properties is empty ") {
    val pe: Properties = Nil
    assert(pe.isEmpty == true)
  }

  test("properties is not empty after add") {
    val pe: Properties = Nil.add("key", "value")
    assert(pe.isEmpty == false)
  }

  test("empty properties throws exc when calling head") {
    val pe: Properties = Nil
    intercept[NoSuchElementException] {
      pe.head
    }
  }

  test("retrieve head from properties") {
    val pe: Properties = Nil.add("key", "value")
    assert(pe.head._1 == "key")
    assert(pe.head._2 == "value")
  }

  test("tail of empty properties throws exc") {
    intercept[NoSuchElementException] {
      Nil.tail
    }
  }

  test("head and tail combination of properties") {
    assert(Nil.add("key1", "value1").add("key2", "value2").tail.head._1 == "key2")
    assert(Nil.add("key1", "value1").add("key2", "value2").tail.head._2 == "value2")
  }

  test("keys") {
    val properties = Nil.add("key1", "value1").add("key2", "value2").add("key3", "value3")
    assert(properties.keys === Set("key1", "key2", "key3"))
  }

  test("get value") {
    val properties = Nil.add("key1", "value1").add("key2", "value2").add("key3", "value3")
    assert(properties.getValue("key1") === Some("value1"))
    assert(properties.getValue("key2") === Some("value2"))
    assert(properties.getValue("key3") === Some("value3"))
    assert(properties.getValue("keyNotExists") === None)
  }
}