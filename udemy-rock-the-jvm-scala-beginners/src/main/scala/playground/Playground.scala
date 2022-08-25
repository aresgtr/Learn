package playground

object Playground {
  def main(args: Array[String]): Unit = {
    val concater: ((String, String) => String) = new Function2[String, String, String] {
      override def apply(a: String, b: String): String = a + b
    }

    println(concater("Hi", "Hello"))
  }

}
