package lectures.part2oop

abstract class L17MyList { // immutable
  /*
    head = first element of the list
    tail = remainder of the list
    isEmpty = is this list empty?
    add(int) => new list with this element added
    toString => a string representation of the list
   */

  def head: Int
  def tail: L17MyList
  def isEmpty: Boolean
  def add(element: Int): L17MyList
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"
}

object Empty extends L17MyList {
  override def head: Int = throw new NoSuchElementException
  override def tail: L17MyList = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def add(element: Int): L17MyList = new Cons(element, Empty)
  override def printElements: String = ""
}

class Cons(h: Int, t: L17MyList) extends L17MyList {
  override def head: Int = h
  override def tail: L17MyList = t
  override def isEmpty: Boolean = false
  override def add(element: Int): L17MyList = new Cons(element, this)
  override def printElements: String =
    if (t.isEmpty) "" + h
    else h + " " + t.printElements
}

object ListTest extends App {
  val list = new Cons(1, Empty)
  println(list.head) //>> 1

  val list2 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(list2.tail.head) //>> 2
  println(list2.add(4).head) //>> 4
  println(list2.isEmpty) //>> false

  println(list2.toString) //>> [1 2 3]
}
