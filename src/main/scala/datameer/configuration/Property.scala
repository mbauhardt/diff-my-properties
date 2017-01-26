package datameer.configuration

import datameer.configuration.PropertyState.State


object PropertyState extends Enumeration {
  type State = Value
  val Added = Value("Added")
  val Updated = Value("Updated")
  val Deleted = Value("Deleted")
}

trait Property {
  def key: String

  def value: String

  def add(key: String, value: String): Property

  def update(key: String, value: String): Property

  def isEmpty: Boolean

  def state: PropertyState.State
}

object Empty extends Property {
  override def key: String = throw new NoSuchElementException("Empty property does not have any key")

  override def value: String = throw new NoSuchElementException("Empty property does not have any value")

  override def add(key: String, value: String): Property = NonEmpty(key, value, PropertyState.Added, Empty)

  override def update(key: String, value: String): Property = throw new NoSuchElementException("Update on an empty property is not allowed")

  override def isEmpty: Boolean = true

  override def state: State = throw new NoSuchElementException("State on an empty property is not allowed")
}

case class NonEmpty(key: String, value: String, state: State, next: Property) extends Property {

  override def add(k: String, v: String): Property = NonEmpty(key, value, PropertyState.Added, next.add(k, v))

  override def update(k: String, v: String): Property = NonEmpty(k, v, PropertyState.Updated, next)

  override def isEmpty: Boolean = false
}
