# Higher-Order-Functions and Curries

In the last videos you saw function types. What keeps us from defining such a function? Nothing.

But how do you read such a function? What kind of parameters does this function have and what is the return type of this 
function?

```scala
val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = ???
```
Return type: ```(Int => Int)```

Parameters: ```(Int, (String, (Int => Boolean)) => Int)```

So the super function takes two parameters, an Int and a function and returns another function.

Such a function that either takes function as parameters or returns functions as a result is called a __Higher Order Function (HOF)__

Example:

- map, flatMap, filter in MyList

#### Function that applies a funciton n times over a value of x
`nTimes(f, n, x)`

`nTimes(f, 3, x) = f(f(f(x))) = `

```scala
@tailrec
def nTimes(f: Int => Int, n: Int, x: Int): Int = {
  if (n <= 0) x
  else nTimes(f, n-1, f(x))
}
```

