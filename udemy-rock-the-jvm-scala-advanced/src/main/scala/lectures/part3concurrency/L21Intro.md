# Intro to Parallel Programming on the JVM

```scala
/*
  interface Runnable {
    public void run()
  }
*/
// JVM threads
val runnable = new Runnable {
  override def run(): Unit = println("Running in parallel")
}
val aThread = new Thread(runnable)

aThread.start()  // It gives the signal to the JVM to start a JVM thread.
                 // It creates a JVM thread, which runs on top of an OS thread

runnable.run()  // doesn't do anything in parallel!
```
```scala
aThread.join()  // blocks until aThread finishes running
```
⬆️ This is how you make sure that a thread has already run before you continue some computation.
```scala
val threadHello = new Thread(() => (1 to 3).foreach(_ => println("hello")))
val threadGoodbye = new Thread(() => (1 to 3).foreach(_ => println("goodbye")))
threadHello.start()
threadGoodbye.start()
```
⬆️ Different runs produce different results!
```text
goodbye
hello
goodbye
goodbye
hello
hello
```
## Executors
- Threads are expensive to start and kill.
- The solution is to reuse them.
- Java Standard Library offers a nice standard API to reuse threads with __executors__ and thread pools.
```scala
val pool = Executors.newFixedThreadPool(nThreads = 10)
pool.execute(() => println("something in the thread pool"))

pool.execute(() => {
  Thread.sleep(1000)
  println("done after 1 second")
})

pool.execute(() => {
  Thread.sleep(1000)
  println("almost done")
  Thread.sleep(1000)
  println("done after 2 seconds")
})
```
```text
done after 1 second
almost done
done after 2 seconds
```
⬆️ "done after 1 second" and "almost done" printed almost at the same time, proof that these actions were executed at the same time.
```scala
pool.shutdown()  // no more actions can be submitted
pool.execute(() => println("should not appear"))
```
⬆️ throws an exception in the calling thread. Threads with "done after 2 seconds" etc. are not affected.
```scala
pool.shutdownNow()
```
⬆️ In this case, all sleeping threads are interrupted.
```scala
println(pool.isShutdown)  //>> true
```
