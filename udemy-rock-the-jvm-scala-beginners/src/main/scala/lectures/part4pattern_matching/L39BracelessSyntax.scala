package lectures.part4pattern_matching

object L39BracelessSyntax {

  // if - expressions
  val anIfExpression = if (2 > 3) "bigger" else "smaller"

  // java-style
  val anIfExpression_v2 =
    if (2 > 3) {
      "bigger"
    } else {
      "smaller"
    }

    // compact
    val anIfExpression_v3 =
      if (2 > 3) "bigger"
      else "smaller"

    // scala 3
    val anIfExpression_v4 =
      if 2 > 3 then
        "bigger"  // higher indentation than the if part
      else
        "smaller"

    val anIfExpression_v5 =
      if 2 > 3 then
        val result = "bigger"
        result
      else
        val result = "smaller"
        result

    // scala 3 one-liner
    val anIfExpression_v6 = if 2 > 3 then "bigger" else "smaller"

    // for comprehensions
    val aForComprehension = for {
      n <- List(1, 2, 3)
      s <- List("black", "white")
    } yield s"$n$s"

    // scala 3
    val aForComprehension_v2 =
      for
        n <- List(1, 2, 3)
        s <- List("black", "white")
      yield s"$n$s"

    // pattern matching
    val meaningOfLife = 42
    val aPatternMatch = meaningOfLife match {
      case 1 => "the one"
      case 2 => "double or nothing"
      case _ => "something eles"
    }

    // scala 3
    val aPatternMatch_v2 = meaningOfLife match
      case 1 => "the one"
      case 2 => "double or nothing"
      case _ => "something eles"

    // methods without braces
    def computeMeaningOfLife(arg: Int): Int =
      val partialResult = 40
      partialResult + 2

    // class definition with significant indentation (same for traits, objects, enums, etc)
    class Animal:
      def eat(): Unit =
        println("I'm eating")

      def grow(): Unit =
        println("I'm growing")

      // if you have 3000 more lines of code, you can have end keyword (optional)
    end Animal  // for if, match, for, methods, classes, traits, enums, objects

    // anonymous classes
    val aSpecialAnimal = new Animal:
      override def eat(): Unit = println("i'm special")

    // indentation: strictly larger indentation
    // 3 spaces + 2 tabs > 2 spaces + 2 tabs
    // 3 spaces + 2 tabs > 3 spaces + 1 tab
    // 3 tabs + 2 spaces ??? 2 tabs + 3 spaces
    // don't mix them
}
