# Currying and Partially Applied Functions

## Curried Functions

Previously we introduced the concept of _functions returning other functions as results_.

```scala
val superAdder: Int => Int => Int =
  x => y => x + y 
```

Now we can define auxiliary functions, for example

```scala
val add3 = superAdder(3)    // Int => Int = y => 3 + y
println(add3(5))
```

We should see `8` in the console

We can also call `superAdder` by itself with multiple parameter lists

```scala
println(superAdder(3)(5))   // >8
```

Scala allows the definition of curried methods out of the box with multiple parameters lists. For example,

```scala
def curriedAdder(x: Int)(y: Int): Int = x + y   // curried method

val add4: Int => Int = curriedAdder(4)
```

Now it gets interesting. This doesn't work if we remove the type annotation `Int => Int`.

```
❗ Error: missing argument list for method curriedAdder in object CurriesPAF
```

The compiler will complain: we are attempting to call this method with fewer parameter lists than the compiler expects

If we add the type annotation back, the compiler is happy.

### Behind the Scene: Lifting

The original problem: `curriedAdder` is a method, `def`

When you call a method, you need to pass in all parameter lists

However,

```scala
val add4: Int => Int = curriedAdder(4)
```
here we convert a method into a function value of type Int => Int

__Lifting__: transforming a method to function

It is also known as __ETA-expansion__

Functions != methods, due to JVM limitation

```scala
// For example
def inc(x: Int) = x + 1
List(1, 2, 3).map(inc)
```

Behind the scene: the compiler does ETA-expansion for us. It turns the `inc` method into a function, and then it uses that function value on map. The compiler rewrites it as

```scala
List(1, 2, 3).map(x => inc(x))
```

### Partial Function Applications: force the compiler to do ETA-expansion when we want

```scala
val add5 = curriedAdder(5) _
```

The underscore `_` will tell the compiler: "Do an ETA-expansion for me!" Then it turns `curriedAdder` into a function value after you applied the first parameter list.

## Exercise

We have 3 different ways of adding 2 numbers

```scala
val simpleAddFunction = (x: Int, y: Int) => x + y
def simpleAddMethod(x: Int, y: Int): Int = x + y
def curriedAddMethod(x: Int)(y: Int) = x + y

// add7: Int => Int = y => 7 + y
// as many different implementations as possible, be creative!
val add7 = (x: Int) => simpleAddFunction(7, x)  // simplest
val add7_2 = simpleAddFunction.curried(7)

val add7_3 = curriedAddMethod(7) _  // Partially Applied Function, PAF
val add7_4 = curriedAddMethod(7)(_) // PAF, alternative syntax

val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
val add7_6 = simpleAddFunction(7, _: Int)   // works as well
```

## Underscores _ are powerful
```scala
def concatenator(a: String, b: String, c: String) = a + b + c
val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")
```
`val insertName` is a function value where I can supply a single parameter

```scala
print(insertName("Daniel"))
```
Prints
```
Hello, I'm Daniel, how are you?
```

Multiple `_` also works

```scala
val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)
println(fillInTheBlanks("Daniel", " Scala is awesome!"))
```
Prints
```
Hello, Daniel Scala is awesome!
```

## Another Exercise
###1. Process a list of numbers and return their string representatoins with different formats. Use %4.2f, %8.6f and %14.12f with a curried formatter function.

```scala
def curriedFormatter(s: String)(number: Double): String = s.format(number)
val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

val simpleFormat = curriedFormatter("%4.2f") _ // lift
val seriousFormat = curriedFormatter("%8.6f") _
val perciseFormat = curriedFormatter("%14.12f") _

println(numbers.map(simpleFormat))
```
The console should print
```text
List(3.14, 2.72, 1.00, 9.80, 0.00)
```
We can simply
```scala
println(numbers.map(curriedFormatter("%14.12f")))
```
We don't need to `println(numbers.map(curriedFormatter("%14.12f") _))`, because the compiler does sweet eta-expansion for us.

###2. differences between

- functions vs mehods
- parameters: by-name vs 0-lambda
```scala
// These are the methods we will use
def byName(n: => Int) = n + 1 // a byName method receives a byName parameter
def byFunction(f: () => Int) = f() + 1  // receives a 0-lambda, nothind to Int

// We will provide the applications of 2 other methods
def method: Int = 42
def parenMathod(): Int = 42
```
Explore calling byName and byFunction with the following expressions
- int
- method
- parenMethod
- lambda
- PAF
```scala
byName(23)  // ok
byName(method)  // also ok
byName(parenMethod()) // ok
byName(parenMethod) // ok but beware, equivalent to byName(parenMethod())
// byName(() =< 42)  // not ok, byName argument of value type is not the same as a function parameter
byName((() => 42)())  // ok
// byName(parenMethod _) // not ok, a function value cannot use as byName parameter
```
```scala
// byFunction(45) // not ok
// byFunction(method) // not ok!!! does not do ETA-expansion!
byFunction(parenMethod) // ok, compiler does ETA-expansion
byFunction(() => 46)  // ok
byFunction(parenMethod _) // also works, but warning - unnecessary
byFunction(parenMethod) // ok
```
