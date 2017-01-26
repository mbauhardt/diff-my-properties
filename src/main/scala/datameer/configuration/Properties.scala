package datameer.configuration

trait Properties {
  def update(key: String, value: String): Properties

  def head: Property

  def tail: Properties

  def isEmpty: Boolean

  def keys: Set[String]

  def foldLeft[B](z: B)(f: (B, Property) => B): B

  def getValue(key: String): Option[String]
}

object Nil extends Properties {
  override def head: Property = throw new NoSuchElementException("Empty Properties has no head")

  override def tail: Properties = throw new NoSuchElementException("Empty Properties has no tail")

  override def isEmpty: Boolean = true

  override def update(s1: String, s2: String): Properties = throw new NoSuchElementException("Empty Properties can not be updated")

  override def keys: Set[String] = Set.empty

  def foldLeft[B](z: B)(f: (B, Property) => B): B = z

  override def getValue(key: String): Option[String] = None
}

case class Cons(val head: Property, val tail: Properties) extends Properties {
  override def isEmpty: Boolean = false

  override def update(key: String, value: String): Properties = {
    if (head.key == key) Properties(head.update(key, value))
    else Cons(Empty.add(head.key, head.value), tail.update(key, value))
  }

  override def foldLeft[B](z: B)(f: (B, Property) => B): B = {
    var acc = z
    var these: Properties = this
    while (!these.isEmpty) {
      acc = f(acc, these.head)
      these = these.tail
    }
    acc
  }

  override def keys: Set[String] = {
    foldLeft(Set.empty: Set[String])((acc: Set[String], property: Property) => {
      acc + (property.key)
    })
  }

  override def getValue(key: String): Option[String] = {
    if (head.key == key) Some(head.value)
    else tail.getValue(key)
  }
}


object Properties {
  def apply(pe: Property): Properties = {
    pe match {
      case Empty => Nil
      case i: NonEmpty => Cons(i, apply(i.next))
    }
  }
}


