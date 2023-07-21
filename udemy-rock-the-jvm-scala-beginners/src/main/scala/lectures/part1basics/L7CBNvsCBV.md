# Call-by-Name and Call-by-Value
## Experiment
```scala
def calledByValue(x: Long): Unit = {
  println("by value: " + x)
  println("by value: " + x)
}

def calledByName(x: => Long): Unit = {
  println("by name: " + x)
  println("by name: " + x)
}

calledByValue(System.nanoTime())
```
`calledByValue` prints the same nanoTime
```text
by value: 1257271899464651
by value: 1257271899464651
```
However,
```scala
calledByName(System.nanoTime())
```
`calledByName` prints the different nanoTime
```text
by name: 1257272059228072
by name: 1257272059263233
```

The reason is, `System.nanoTime()` is evaluated every time for `callByName`

## Definition

_Call-by-Name_ delays the evaluation of the expression passed as a parameter, and it's used every time

- It is extremely useful in _lazy_ streams and in things that might _fail_.

```scala
def infinite(): Int = 1 + infinite()
def printFirst(x: Int, y : => Int) = println(x)

printFirst(infinite(), 34)  // This will crash with StackOverflowError
printFirst(34, infinite())  // This will be fine, infinite() will never execute
```