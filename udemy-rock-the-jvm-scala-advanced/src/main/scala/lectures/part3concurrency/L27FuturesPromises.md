# Futures and Promises

```scala
// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

// ...

def calculateMeaningOfLife: Int = {
  Thread.sleep(2000)
  42
}

val aFuture = Future {
  calculateMeaningOfLife // calculates the meaning of life on ANOTHER thread
}  // (global) which is passed by the compiler

println(aFuture.value)  // Option[Try[Int]]

println("Waiting on the future")
aFuture.onComplete {
  case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
  case Failure(e) => println(s"I have failed with $e")
}

Thread.sleep(3000)  // force the main thread to wait for future to complete
```
Which prints:
```text
None
Waiting on the future
the meaning of life is 42
```

## Part 2: mini social network example

```scala
case class Profile(id: String, name: String) {
  def poke(anotherProfile: Profile): Unit =
    println(s"${this.name} poking ${anotherProfile.name}")
}

object SocialNetwork {
  // "database"
  val names = Map(
    "fb.id.1-zuck" -> "Mark",
    "fb.id.2-bill" -> "Bill",
    "fb.id.0-dummy" -> "Dummy"
  )
  val friends = Map(
    "fb.id.1-zuck" -> "fb.id.2-bill"
  )

  val random = new Random()

  // potentially expensive API calls
  def fetchProfile(id: Stirng): Future[Profile] = Future {
    // fetching from the DB
    Thread.sleep(random.nextInt(300))
    Profile(id, names(id))
  }

  def fetchBestFriend(profile: Profile): Future[Profile] = Future {
    Thread.sleep(random.nextInt(400))
    val bfId = friends(profile.id)
    Profile(bfId, names(bfId))
  }

}

// client: mark to poke bill
val mark = SoialNetwork.fetchProfile(id="fb.id.1-zuck")
mark.onComplete {
  case Success(markProfile) => {
    val bill = SocialNetwork.fetchBestFriend(markProfile)
    bill.onComplete {
      case Success(billProfile) => markProfile.pole(billProfile)
      case Failure(e) => e.printStackTrace()
    }
  }

  case Failure(ex) => ex.printStrackTrace()
}

Thread.sleep(1000)
```
Which prints
```text
Mark poking Bill
```
⬆️ This is very ugly with nested code. To improve this, we need 👇

### Functional composition of futures

```scala
// map, flatMap, filter
val nameOnTheWall = mark.map(profile => profile.name)

val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

// for-comprehensions
for {
  mark <- SocialNetwork.fetchProfile(id="fb.id.1-zuck")
  bill <- SocialNetwork.fetchBestFriend(mark)
} mark.poke(bill)    // ← this is so much cleaner than the nested code above

// fallbacks
val aProfileNoMatterWhat = SocialNetwork.fetchProfile(id="unknown id").recover {
  case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
}

val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile(id="unknown id").revocerWith {
  case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
}

val fallbackResult = SocialNetwork.fetchProfile(id="unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))
```

## Part 3: how to block on Futures and how to control them with Promise pattern

Why we block on Futures?
- For critical operations like bank transfers (anything that is transaction) 💴
- you make sure the operation is fully complete before move on to display results / compute additional values

### Online banking app example 💳

```scala
case class User(name: String)
case class Transaction(sender: String, receiver: String, amount: Double, status: String)

object BankingApp {
  val name = "Rock the JVM banking"

  def fetchUser(name: String): Future[User] = Future {
    // simulate fetching from the DB
    Thread.sleep(500)
    User(name)
  }

  def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
    // simulate some processes
    Thread.sleep(1000)
    Transaction(user.name, merchantName, amount, "SUCCESS")
  }

  def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
    // fetch the user from the DB
    // create a transaction
    // WAIT for the transaction to finish
    val transactionStatusFuture = for {
      user <- fetchUser(username)
      transaction <- createTransaction(user, mechantName, cost)
    } yield transaction.status
    Await.result(transactionStatusFuture, 2.seconds)  // need import scala.concurrency.duration._
  }

}

println(BankingApp.purchase(username="Daniel", item="iPhone 12", merchantName="rock the jvm store", cost=3000))
```
Which prints:
```text
SUCCESS
```

### Promises

```scala
val promise = Promise[Int]()  // "controller" over a future
val future = promise.future

// thread 1 - "consumer"
future.onComplete {
  case Success(r) => println("[consumer] I've received " + r)
}

// thread 2 - "producer"
val producer = new Thread(() -> {
  println("[producer] crunching numbers...")
  Thread.sleep(500)
  // "fufilling" the promise
  promise.success(42)
  println("[producer] done")
})

producer.start()
Thread.sleep(1000)
```
Which prints:
```text
[producer] crunching numbers...
[producer] done
[consumer] I've received 42
```
