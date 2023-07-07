# Partial Functions

A little warm up
```scala
val aFunction = (x: Int) => x + 1
```
This is `Function1[Int, Int]` or `Int => Int` equivalent

If we want to set a restriction, here is a clunky implementation
```scala
val aFussyFunction = (x: Int) =>
  if (x == 1) 42
  else if (x == 2) 56
  else if (x == 5) 999
  else throw new FunctionNotApplicableException

// slightly better (this is a total function implementation)
val aNicerFussyFunction = (x: Int) => x match {
  case 1 => 42
  case 2 => 56
  case 5 => 999
}
// equivalent to {1, 2, 5} => Int, this is called a partial function from Int to Int

class FunctionNotApplicableException extends Runtime  // We implement this ourselves
```
To improve this using Scala features
```scala
val aPartialFunction: PartialFunction[Int, Int] = {
  // partial function value
  case 1 => 42
  case 2 => 56
  case 5 => 999
}
```
```scala
println(aPartialFunction(2))
```
This will print
```text
56
```
However if we
```scala
println(aPartialFunction(57273))
```
This will crash with **MatchError**

## Utilities
```scala
println(aPartialFunction.isDefinedAt(67))
```
This will return `false`. It is useful without crashing program.

### PFs can be lifted to total functions, returning Options
```scala
val lifted = aPartialFunction.lift  // Int => Option[Int]
println(lifted(2))
println(lifted(98))
```
Which prints
```text
Some(56)
None
```
### "orElse" if you want to chain multiple partial functions (打补丁)
```scala
val pfChain = aPartialFunction.orElse[Int, Int] {
  case 45 => 67
}

println(pfChain(2))
println(pfChain(45))
```
Which prints
```text
56
67
```

### Basically, Partial Functions extend normal functions
We can supply a partial function literal from Int to Int, because PFs are a subtype of total functions.
```scala
val aTotalFunction: Int => Int = {
  case 1 => 99
}
```
#### As a side effect of above, Higher-Order Functions accept PFs
```scala
val aMappedList = List(1, 2, 3).map {
  case 1 => 42
  case 2 => 78
  case 3 => 1000
}

println(aMappedList)
```
Which prints
> List(42, 78, 1000)

However if we replace `case 3` to `case 5`, it will crash with `MatchError`

### Note

- PF can only have ONE parameter type

## Exercise
1. Construct a PF instance yourself (anonymous class)
```scala
val aManualFussyFunction = new PartialFunction[Int, Int] {
  override def apply(x: Int): Int = x match {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  override def isDefinedAt(x: Int): Boolean =
    x == 1 || x == 2 || x == 3
}
```
2. dumb chatbot as a PF
```scala
val chatbot: PartialFunction[String, String] = {
  case "hello" => "Hi, my name is HAL9000"
  case "goodbye" => "Once you start talking to me, there is no return, human!"
  case "call mom" => "Unable to find your phone without your credit card"
}

scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: " + chatbot(line)))
```
If we input `hello` in the console, it will print
> Hi, my name is HAL9000

If we input `get out`, which chatbot doesn't recognize, it will raise `MatchError`

A nicer way to implement the prompt message:
```scala
scala.io.Source.stdin.getLines().map(chatbot).foreach(println)
```
