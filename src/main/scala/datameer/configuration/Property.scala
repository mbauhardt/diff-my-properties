package datameer.configuration

trait Property {
  def key: String

  def value: String

  def add(key: String, value: String): Property

  def isEmpty: Boolean
}

object Empty extends Property {
  override def key: String = throw new NoSuchElementException("Empty property does not have any key")

  override def value: String = throw new NoSuchElementException("Empty property does not have any value")

  override def add(key: String, value: String): Property = NonEmpty(key, value, Empty)

  override def isEmpty: Boolean = true
}

case class NonEmpty(key: String, value: String, next: Property) extends Property {

  override def add(k: String, v: String): Property = NonEmpty(key, value, next.add(k, v))

  override def isEmpty: Boolean = false
}
