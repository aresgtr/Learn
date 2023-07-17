# Lazy Evaluation
## Concepts
```scala
// If we run this, the program will crash.
object LazyEvaluation extends App {
  val x: Int = throw new RuntimeException
}
```
```scala
// With keyword lazy, the program will not crash.
object LazyEvaluation extends App {
  lazy val x: Int = throw new RuntimeException
}
```
### 1. _lazy_ delays the evaluation of values.
```scala
// If we print the value of x, it will again crash
object LazyEvaluation extends App {
  lazy val x: Int = throw new RuntimeException
}
println(x)
```
### 2. _lazy_ only execute once, when it is used for the 1st time
```scala
lazy val x: Int = {
  println("hello")
  42
}
println(x)
println(x)
```
The first `println` prints
```text
hello
42
```
But the second `println` only prints
```text
42
```
## Examples of Implications
### 1. Side effects
```scala
def sideEffectCondition: Boolean = {
  println("Boo")
  true
}
def simpleCondition: Boolean = false

lazy val lazyCondition = sideEffectCondition

println(if (simpleCondition && lazyCondition) "yes" else "no")
```
This prints `no`, but not `Boo`
```text
no
```
This is because `simpleCondition` is already _false_, so `lazyCondition` is never executed.
Thus "Boo" never prints.

❗Be very careful when using __lazy val__ with side effects.

### 2. In conjunction with call by name
```scala
def byNameMethod(n: => Int): Int = n + n + n + 1
def retrieveMagicValue = {
  // side effect or a long computation
  Thread.sleep(1000)
  42
}

println(byNameMethod(retrieveMagicValue))
```
We wait for around __3 sec__, then we see
```text
127
```
Somethimes the waiting is unnecessary. If we use __lazy val__
```scala
def byNameMethod(n: => Int): Int = {
  lazy val t = n
  t + t + t + 1
}
def retrieveMagicValue = {
  // side effect or a long computation
  Thread.sleep(1000)
  42
}

println(byNameMethod(retrieveMagicValue))
```
Then we only wait once, for __1 sec__, and we see
```text
127
```
This method, __CALL BY NEED__, is useful when you want to evaluate your parameter only when you need, and reuse the same value in the rest of your code.

### Filtering with _lazy val_
```scala
def lessThan30(i: Int): Boolean = {
  println(s"$i is less than 30?")
  i < 30
}

def greaterThan20(i: Int): Boolean = {
  println(s"$i is greater than 20?")
  i > 20
}

val numbers = List(1, 25, 40, 5, 23)
val lt30 = numbers.filter(lessThan30) //>> List(1, 25, 5, 23)
val gt20 = lt30.filter(greaterThan20)
println(gt20)
```
Prints
```text
1 is less than 30?
25 is less than 30?
40 is less than 30?
5 is less than 30?
23 is less than 30?
1 is greater than 20?
25 is greater than 20?
5 is greater than 20?
23 is greater than 20?
List(25, 23)
```

```scala
val lt30lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
val gt20lazy = lt30lazy.withFilter(greaterThan20)
println(gt20lazy)
```
Prints
```text
scala.collection.TraversableLike$WithFilter@2f7a2457
```
This is actually the wrapper of withFilter. However, we don't see any side effects. The filtering does not happen.

If we don't `println(gt20lazy)`. Instead, we `gt20lazy.foreach(println)`, this forces the filtering to execute.

```text
1 is less than 30?
1 is greater than 20?
25 is less than 30?
25 is greater than 20?
25
40 is less than 30?
5 is less than 30?
5 is greater than 20?
23 is less than 30?
23 is greater than 20?
23
```
The order being executed shows that, the side effects and predicates are checked on a by-need basis.

### for-comprehensions use withFilter with guards
```scala
for {
  a <- List (1, 2, 3) if a % 2 == 0 // use lazy vals!
} yield a + 1
// This translates to
List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1)
```
withFilter returns with a wrapper. So it works with map to give us back List[Int

## Exercise
Implement a lazily evaluated, singly linked STREAM of elements.
```scala
/*
  naturals = MyStream.from(1)(x => x + 1)  // stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println)  // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println)  // will crash - infinite!
  naturals.map(_ * 2)  // stream of all even numbers (potentially infinite)
 */
abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]
  
  def #::[B >: A](element: B): MyStream[B]  // prepend operator
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate 2 streams
  
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]
  
  def take(n: Int): MyStream[A] // takes the first n elements out of this steam
  def takeAsList(n: Int): List[A]
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}
```