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
      pe.key
    }
  }

  test("retrieve key/value from the property") {
    val pe: Property = Empty.add("key", "value")
    assert(pe.key == "key")
    assert(pe.value == "value")
  }
}