package lectures.part4implicits

import java.util.Date

object L38JSONSerialization extends App {

  /*
    Users, posts, feeds
      serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  // Because our codebase is huge, it would not make sense to write implementations in the case classes themselves.
  // We want this to be extensible and we want other people to be able to write to our codebase.
  // So we decide to use the Type Class pattern.

  /*
    Steps:
    1 - intermediate data types: Int, String, List, Date
    2 - type classes for conversion to intermediate data types
    3 - serialize to JSON
   */

  // 1 - intermediate data type
  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
      {
        name: "John"
        age: 22
        friends: [ ... ]
        latestPost: {
          content: "Scala Rocks"
          date: ...
        }
      }
     */
    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{", ",", "}")
  }

  // test
  val data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala Rocks!"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify) //>> {"user":"Daniel","posts":["Scala Rocks!",453]}

  // 2 - type class
  //      1 * type class
  //      2 * tpe class instances (implicit)
  //      3 * pimp library to use type class instances

  // 2.1
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2.2

  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created:" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> UserConverter.convert(feed.user), // TODO, quite clunky
      "posts" -> JSONArray(feed.posts.map(PostConverter.convert)) // TODO, makes FeedConverter depend on the exact implementation of PostConverter, this is unnecessary dependency
    ))
  }

  /**
   * Notes to improve FeedConverter
   *  first, we need to lift 2.3 a bit before the FeedConverter
   *  then the FeedConverter can be improved as follows:
   *
   *  implicit object FeedConverter extends JSONConverter[Feed] {
   *    override def convert(feed: Feed): JSONValue = JSONObject(Map(
   *      "user" -> feed.user.toJSON,
   *      "posts" -> JSONArray(feed.posts.map(_.toJSON))
   *  ))
  }
   */

  // 2.3 conversion
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // 3 - call stringify on result
  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("hello", now),
    Post("look at this cute puppy", now)
  ))

  println(feed.toJSON.stringify)
  //>> {"user":{"name":"John","age":34,"email":"john@rockthejvm.com"},"posts":[{"content":"hello","created:":"Sun Apr 07 17:22:50 CST 2024"},{"content":"look at this cute puppy","created:":"Sun Apr 07 17:22:50 CST 2024"}]}
}
