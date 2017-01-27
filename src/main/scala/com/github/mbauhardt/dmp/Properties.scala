package com.github.mbauhardt.dmp

trait Properties {

  def head: (String, String)

  def tail: Properties

  def add(key: String, value: String): Properties

  def isEmpty: Boolean

  def keys: Set[String]

  def foldLeft[B](z: B)(f: (B, (String, String)) => B): B

  def getValue(key: String): Option[String]
}

object Nil extends Properties {

  override def add(key: String, value: String): Properties = Cons(key, value, Nil)

  override def isEmpty: Boolean = true

  override def head: (String, String) = throw new NoSuchElementException("Empty properties does not have any head")

  override def tail: Properties = throw new NoSuchElementException("Empty properties does not have any tail")

  override def keys: Set[String] = Set.empty

  def foldLeft[B](z: B)(f: (B, (String, String)) => B): B = z

  override def getValue(key: String): Option[String] = None
}

case class Cons(key: String, value: String, tail: Properties) extends Properties {

  override def add(k: String, v: String): Properties = Cons(key, value, tail.add(k, v))

  override def isEmpty: Boolean = false

  override def head: (String, String) = (key, value)

  override def keys: Set[String] = {
    foldLeft(Set.empty: Set[String])((acc: Set[String], next: (String, String)) => {
      acc + next._1
    })
  }

  override def foldLeft[B](z: B)(f: (B, (String, String)) => B): B = {
    var acc = z
    var these: Properties = this
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
