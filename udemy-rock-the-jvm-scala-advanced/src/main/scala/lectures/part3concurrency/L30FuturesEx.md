# Futures, Part 4 + Exercises

## 1) Fulfill a Future IMMEDIATELY with a value

```scala
def fulfillImmediately(value: T): Future[T] = Future(value)

// Alternative - this is even faster, because the Future is fulfilled synchronously (no thread is needed)
Future.successful(value)
```

## 2) 2 Futures in sequence

inSequence(fa, fb)

This returns the 2nd future while making sure the 1st future has finished.

```scala
def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = {
  first.flatMap(_ => second)
}
```

## 3) first(fa, fb) => new future with the first value of the 2 futures

```scala
def first(fa: Future[A], fb: Future[A]): Future[A] = {
  val promise = Promise[A]

  fa.onComplete {
    case Success(r) => try {
      promise.success(r)
    } catch {
      case _ =>
    }
    case Failure(t) => try {
      promise.failure(t)
    } catch {
      case _ =>
    }
  }

  fb.onComplete {
    case Success(r) => try {
      promise.success(r)
    } catch {
      case _ =>
    }
    case Failure(t) => try {
      promise.failure(t)
    } catch {
      case _ =>
    }
  }

  promise.future
}
```
A nicer implementation:
```scala
def first(fa: Future[A], fb: Future[A]): Future[A] = {
  val promise = Promise[A]

  def tryComplete(promise: Promise[A], result: Try[A]) = result match {
    case Success(r) => try {
      promise.success(r)
    } catch {
      case _ =>
    }
    case Failure(t) => try {
      promise.failure(t)
    } catch {
      case _ =>
    }
  }

  fa.onComplete(promise.tryComplete(_))
  fb.onComplete(promise.tryComplete(_))

  promise.future
}
```
A even nicer implementation with built-in functions:
```scala
def first(fa: Future[A], fb: Future[A]): Future[A] = {
  val promise = Promise[A]

  fa.onComplete(promise.tryComplete)
  fb.onComplete(promise.tryComplete)

  promise.future
}
```
To sum up: the implementation is:
1. creating a promise
2. calling onComplete on both of our argument Futures
3. the first Future complete with succeed in calling tryComplete
4. that will set the resulting future (promise.future)
5. then fb is late, tryComplete doesn't do anything
6. In the end, promise.future will hold either the value or the exception returned by the first finished promise.

## 4) last out of the 2 Futures

```scala
def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
  // promise #1 which both Futures will try to complete
  // promies #2 which the LAST Future will complete
  val bothPromise = Promise[A]
  val lastPromise = Promise[A]

  fa.onComplete(result => {
    if (!bothPromise.tryComplete(result))
      lastPromise.complete(result)
  })

  fb.onComplete(result => {
    if (!bothPromise.tryComplete(result))
      lastPromise.complete(result)
  })

  lastPromise.future
}
```
To simplyfy this:
```scala
def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
  // promise #1 which both Futures will try to complete
  // promies #2 which the LAST Future will complete
  val bothPromise = Promise[A]
  val lastPromise = Promise[A]

  val checkAndComplete = (result: Try[A]) =>
    if (!bothPromise.tryComplete(result))
      lastPromise.complete(result)

  fa.onComplete(checkAndComplete)
  fb.onComplete(checkAndComplete)

  lastPromise.future
}
```
Let's test the above code
```scala
val fast = Future {
  Thread.sleep(100)
  42
}

val slow = Future {
  Thread.sleep(200)
  45
}

first(fast, slow).foreach(println)
last(fast, slow).foreach(println)

Thread.sleep(1000)
```
Which prints
```text
42
45
```

## 5) retry until

```scala
def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
  action()
  .filter(condition)
  .recoverWith {
    case _ => retryUntil(action, condition)
  }

val random = new Random()
val action = () => Future {
  Thread.sleep(100)
  val nextValue = random.nextInt(100)
  println("generated " + nextValue)
  nextValue
}

retryUntil(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))
Thread.sleep(10000)
```
Which prints:
```text
generated 42
generated 93
generated 12
generated 44
generated 99
...
generated 2
settled at 2
```
⬆️ settled at 2 because it is the 1st one which passes the predicate.
- All the other numbers, 42, 93, 12, etc. did not pass the predicate.
- So we forced the action to run agin by calling _retryUntil_ recursively.

## Q&A
> _Mukesh_  [17👍]
>
> __I think implementation of 2nd exercise in not right in lecture "30. Futures, Part 4 + Exercises"__
>
> At 5:19 in "Lecture 30. Futures, Part 4 + Exercises":
>
> Exercise 2: inSequence(fa, fb) // Future fb should run after Future fa has ended.
>
> Solution mentioned by you in lecture:
>
> ```scala
> def inSequence[A, B](first: Future[A], second: Future[B]) =
>   first.flatMap(_ => second)
> 
> val firstFuture = Future {
>   println("first future initializing...")
>   Thread.sleep(2000)
>   println("first future returning 123")
>   123
> }
> 
> val secondFuture = Future {
>   println("second future initializing...")
>   Thread.sleep(500)
>   println("second future returning 456")
>   456
> }
> 
> inSequence(firstFuture, secondFuture)
> ```
>
> Output of the above code:
>
> ```text
> first future initializing...
> second future initializing...
> second future returning 456
> first future returning 123
> ```
>
> In above output, we can see that future execution started when we declared variable "firstFuture" and "secondFuture".
>
> So, in order to ensure that "secondFuture" is run only after "firstFuture", we have to use call by name in method "inSequence" and return "firstFuture" & "secondFuture" from "def".
>
> ```scala
> def inSequence[A, B](firstFuture: => Future[A], secondFuture: => Future[B]) = {
>   for {
>     firstValue <- firstFuture
>     secondValue <- secondFuture
>   } println(s"firstValue: ${firstValue} secondValue: ${secondValue}")
> }
> 
> def firstFuture = Future {
>   println("first future initializing...")
>   Thread.sleep(2000)
>   println("first future returning 123")
>   123
> }
>  
> def secondFuture = Future {
>   println("second future initializing...")
>   Thread.sleep(500)
>   println("second future returning 456")
>   456
> }
> inSequence(firstFuture, secondFuture)
> ```
>
> Output of above code:
>
> ```text
> first future initializing...
> first future returning 123
> second future initializing...
> second future returning 456
> firstValue: 123 secondValue: 456
> ```
>
> In my above implementation, it is ensured that first future finishes first and only then second future runs. So, i think the implementation provided in Lecture is not right. Please let me know if i am incorrect.
>
> _Daniel_ - Instructor ⭐Answer [6👍]
> 
> Hi Mukesh, there's indeed an issue in the exercise in that it's not 100% clear. I wanted us to return the second result only after the first has finished. Even though the second future finishes first, its result will only be used after the first finishes. The printlns don't show the real deal.
> 
> However, you make a really good point that by the time you call inSequence(future1, future2), they've both already started, which leads to the confusion of the exercise, in the form of "don't start the second future until the first finishes". You are correct to use by-name arguments there.
> 
> In the next update to the course, I will make the appropriate changes. Thanks for pointing this out.

> _Pavel_ [2👍]
> 
> __fulfillImmediately__
> 
> Won't it be better to use `Future.successfull(value)` here? `Future.apply` starts a new async computation.
>
>  _Daniel_ - Instructor ⭐Answer [1👍]
>
> A good observation - yes. `Future.apply` also completes virtually immediately if the value is already computed, as is the case with our exercise. `Future.successful` returns a pre-completed future.
>
> _Nicky_ [1👍]
>
> Indeed  `(1 to 10000).count(_ => Future(42).isCompleted)` is often between 5000 and 9500, so if we need guarantees, `Future.successful` is the way.
