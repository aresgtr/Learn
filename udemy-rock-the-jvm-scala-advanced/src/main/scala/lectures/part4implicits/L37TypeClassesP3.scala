package lectures.part4implicits


object L37TypeClassesP3 extends App {

  /*
    Some helper functions
   */

  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  implicit object ImplicitUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  implicit object ImplicitIntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  /*
    Part 3 Start from Here
   */

  // in the first demo, 👇 we don't have this "`implicit` serializer" keyword.
  implicit class HTMLEnrichment[T](value: T) {
    def anotherToHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(john.anotherToHTML(UserSerializer)) //>> <div>John (32 yo) <a href=john@rockthejvm.com/> </div>
  /* Re-written as: println(new HTMLEnrichment[User](john).anotherToHTML(UserSerializer)) */

  /**
   * // If we use implicit parameter inside def anotherToHTML
   *
   * implicit class HTMLEnrichment[T](value: T) {
   *  def anotherToHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
   * }
   *
   * println(john.anotherToHTML)  // This will work! COOL!
   */

  // This solves a lot of problems:
  // - extends to new types 👇
  println(2.anotherToHTML)  //>> <div style: color=blue>2</div>

  // - choose implementation, very flexible
  println(john.anotherToHTML(PartialUserSerializer))  //>> <div>John</div>

  // - super expressive!
  //    Type classes pattern composes several parts:
  //    * type class itself
  //        example: HTMLSerializer[T] { .. }
  //    * type class instances (some of which are implicit)
  //        example: UserSerializer, IntSerializer
  //    * conversion with implicit classes
  //        example: HTMLEnrichment

  /*
    Exercise - improve the Equal Type Class with an implicit conversion class

    implement:
      ===(anotherValue: T)
      !==(anotherValue: T)
   */
  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  implicit object ImplicitNameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = !equalizer.apply(value, other)
  }

  println(john === anotherJohn) //>> true
  /*
    Re-write steps by the compiler:
    1. john.===(anotherJohn)
    2. new TypeSafeEqual[User](john).===(anotherJohn)
    3. new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)

    Advantage: type safe!
      example:
        println(john === 43) ⬅️ this not compile
   */

  // Some other stings:
  // context bounds
  def htmlBoilerPlate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.anotherToHTML(serializer)}</body></html>"

  // a better way
  def htmlSugar[T : HTMLSerializer](content: T): String =
    s"<html><body> ${content.anotherToHTML}</body></html>"

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]

  // and then we can improve the htmlSugar with serializer
  def htmlSugarImp[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    s"<html><body> ${content.anotherToHTML(serializer)}</body></html>"
  }
}
