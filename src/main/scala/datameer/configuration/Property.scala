package datameer.configuration

trait Property {

  def head: (String, String)

  def tail: Property

  def add(key: String, value: String): Property

  def isEmpty: Boolean

  def keys: Set[String]

  def foldLeft[B](z: B)(f: (B, (String, String)) => B): B

  def getValue(key: String): Option[String]

}

object Empty extends Property {

  override def add(key: String, value: String): Property = NonEmpty(key, value, Empty)

  override def isEmpty: Boolean = true

  override def head: (String, String) = throw new NoSuchElementException("Empty property does not have any value")

  override def tail: Property = throw new NoSuchElementException("Empty property does not have any value")

  override def keys: Set[String] = Set.empty

  def foldLeft[B](z: B)(f: (B, (String, String)) => B): B = z

  override def getValue(key: String): Option[String] = None
}

case class NonEmpty(key: String, value: String, tail: Property) extends Property {

  override def add(k: String, v: String): Property = NonEmpty(key, value, tail.add(k, v))

  override def isEmpty: Boolean = false

  override def head: (String, String) = (key, value)

  override def keys: Set[String] = {
    foldLeft(Set.empty: Set[String])((acc: Set[String], next: (String, String)) => {
      acc + next._1
    })
  }

  override def foldLeft[B](z: B)(f: (B, (String, String)) => B): B = {
    var acc = z
    var these: Property = this
    while (!these.isEmpty) {
      acc = f(acc, these.head)
      these = these.tail
    }
    acc
  }

  override def getValue(k: String): Option[String] = {
    if (k == key) Some(value)
    else tail.getValue(k)
  }
}

object Property {
  def apply(t: (String, String)): Property = NonEmpty(t._1, t._2, Empty)
}

