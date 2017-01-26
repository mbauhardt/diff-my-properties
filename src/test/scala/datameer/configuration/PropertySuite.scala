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
    assert(pe.state == PropertyState.Added)
  }

  test("empty property throws exc when calling key") {
    val pe: Property = Empty
    intercept[NoSuchElementException] {
      pe.key
    }
  }

  test("retrieve key/value from the property") {
    val pe: Property = Empty.add("key", "value")
    assert(pe.key == "key")
    assert(pe.value == "value")
  }

  test("Add Update Add") {
    val prop1 = Empty.add("key1", "value1")
    val update = prop1.update("foo", "bar")

    assert(prop1.key === "key1")
    assert(prop1.value === "value1")
    assert(prop1.state == PropertyState.Added)

    assert(update.key === "foo")
    assert(update.value === "bar")
    assert(update.state == PropertyState.Updated)
  }

}