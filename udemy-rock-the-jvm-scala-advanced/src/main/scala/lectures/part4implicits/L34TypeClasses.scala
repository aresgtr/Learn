package lectures.part4implicits

object L34TypeClasses extends App {

  // A type class is a trait that takes a type, and describes what operations can be applied to that type.

  /*
    Example
    - Imagine we are the backend developers of a small social network
    - We decide to do server-side rendering for various elements on the page
   */

  // a general trait for all the data structures we can show
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com").toHTML
  /*
    The above ↑↑↑ works but has 2 disadvantages
      1. only works for the types WE write
        - For other types (e.g. Java standard dates or some other types in other libraries), we have to write conversions
      2. ONE implementation out of quite a number
   */

  // Option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHTML(value: Any) = value match {
      case User(n, a, e) =>
      case java.util.Date =>
      case _ =>
    }
  }
  /*
    ↑↑↑ disadvantages
      1. lost type safety
      2. When we add a new data structure, we need to modify the code
      3. still one implementation for each type

    Now ↓↓↓ is a better design
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john)) //>> <div>John (32 yo) <a href=john@rockthejvm.com/> </div>

  // Advantages
  // 1. we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2. we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  /*
    The HTMLSerializer thing is called a TYPE CLASS

    Normal Class vs. Type Class
      a normal class
                    - something must have some methods and properties, in order to belong to that type
                    - For example, String "john" must support .length operation
                    - Then the type checker for the compiler can use this info at compile time to find errors
                    - ↑ is called "static type checking"
      a type class
                    - describes a collection of properties or methods that a type must have
   */

  // In general, a type class looks like this
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  /*
    Exercise - equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }


  /**
   * L35. Part 2
   */
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    // benefit: access to the entire type class interface
    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }
  println(HTMLSerializer.serialize(42)(IntSerializer))  //>> <div style: color=blue>42</div>

  // Why not to let the compiler figure out the serializer for me?
  implicit object ImplicitIntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }
  println(HTMLSerializer.serialize(42))  //>> <div style: color=blue>42</div>

  // The compiler then can fetch whatever serializer for us.
  implicit object ImplicitUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }
  println(HTMLSerializer.serialize(john)) //>> <div>John (32 yo) <a href=john@rockthejvm.com/> </div>

  // benefit: access to the entire type class interface
  println(HTMLSerializer[User].serialize(john)) //>> <div>John (32 yo) <a href=john@rockthejvm.com/> </div>


  /*
    Exercise: implement the Type Class pattern for the Equality Type Class
   */
  object Equal {  // companion
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  // Then we need to make one of the type class instance implicit
  implicit object ImplicitNameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")
  println(Equal.apply(john, anotherJohn)) //>> true

  /**
   *  This is called AD-HOC Polymorphism:
   *    depending on the actual type of the values being compared,
   *    the compiler takes care to fetch the correct type class instance for our types.
   */
}
