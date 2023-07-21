# Call-by-Name and Call-by-Value
```scala
def calledByValue(x: Long): Unit = {
  println("by value: " + x)
  println("by value: " + x)
}

def calledByName(x: => Long): Unit = {
  println("by value: " + x)
  println("by value: " + x)
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
by value: 1257272059228072
by value: 1257272059263233
```
