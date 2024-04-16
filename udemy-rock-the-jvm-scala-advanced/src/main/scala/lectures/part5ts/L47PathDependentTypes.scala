package lectures.part5ts

object L47PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType  // all valid

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String  // only for alias

    2
  }

  // 1 - per-instance
  val o = new Outer
  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
//  val otherInner: oo.Inner = new o.Inner  // not compile, different types

  o.print(inner)
//  oo.print(inner) // not compile, different types

  // ⬆️ Path-dependent types

  // 2 - Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)  // both valid


  /*
    Exercise
      DB keyed by Int or String, but maybe others
   */

  // current design
  {
    trait Item[Key]
    trait IntItem extends Item[Int]
    trait StringItem extends Item[String]
  }

  /*
    Your goal
      def get[ItemType](key: Key): ItemType

      get[IntItem](42)  // ok
      get[StringItem]("home") // ok

      get[IntItem]("scala") // not ok

    Hint:
      use path-dependent types
      abstract type members and/or type aliases
   */
  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42)
  get[StringItem]("home")

//  get[IntItem]("scala") // not compile, good
}
