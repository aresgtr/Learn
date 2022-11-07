package lectures.part3fp

object L32TuplesAndMaps extends App {

  // Tuples - finite ordered "lists"
  val aTuple = new Tuple2(2, "hello, Scala")  // Tuple2[Int, String] = (Int, String)
  val aTuple2 = (2, "hello, Scala") // equivalent

  // Tuples can group at most 22 elements of different types
  // Why 22? Because they're used in conjunction with function types

  println(aTuple._1)  // first element
  println(aTuple.copy(_2 = "goodbye Java")) //>> (2,goodbye Java)
  println(aTuple.swap)  //>> (hello, Scala,2)

  // Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phonebook = Map(("Jim", 555), "Daniel" -> 789).withDefaultValue(-1) // guard for not crash
  // a -> b is sugar for (a, b)
  println(phonebook)  //>> Map(Jim -> 555, Daniel -> 789)

  // Map operations
  println(phonebook.contains("Jim"))  //>> true
  println(phonebook("Jim")) //>> 555

  // add a paring
  val newPairing = "Mary" -> 678
  val newPhonebook = phonebook + newPairing
  println(newPhonebook) //>> Map(Jim -> 555, Daniel -> 789, Mary -> 678)

  // Functionals on Maps
  // map, flatMap, filter

  println(phonebook.map(pair => pair._1.toLowerCase -> pair._2))  //>> Map(jim -> 555, daniel -> 789)

  // filterKeys
  println(phonebook.view.filterKeys(x => x.startsWith("J")).toMap)  //>> Map(Jim -> 555)

  // mapValues
  println(phonebook.view.mapValues(number => number * 10).toMap)  //>> Map(Jim -> 5550, Daniel -> 7890)

  // Conversions to other collections
  println(phonebook.toList) //>> List((Jim,555), (Daniel,789))
  println(List(("Daniel", 555)).toMap)  //>> Map(Daniel -> 555)

  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))  // group the names, by whoever have the same char at 0 in their name
  //>> HashMap(J -> List(James, Jim), A -> List(Angela), M -> List(Mary), B -> List(Bob), D -> List(Daniel))

  /*
    Exercise
    1.  What would happen if I had two original entries  "Jim" -> 555 and "JIM" -> 900?
    2.  Overly simplified social network based on maps.
        Person = String
        - add a person to the network
        - remove
        - friend (mutual)
        - unfriend

        - number of friends of a person
        - person with most friends
        - how many people have NO friends
        - if there is a social connection between two people (direct or not)
   */
  val phonebook2 = Map(("Jim", 555), ("JIM", 9000))
  println(phonebook2.map(pair => pair._1.toLowerCase -> pair._2)) //>> Map(jim -> 9000)
  // You should make sure when you map your keys, the resulting keys do not overlap. Otherwise you might lose data.
  // !!! careful with mapping keys

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =
    network + (person -> Set())

  def friend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)

    network + (a -> (friendsA + b)) + (b -> (friendsB + a))
  }

  def unfriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)

    network + (a -> (friendsA - b)) + (b -> (friendsB - a))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =
      if (friends.isEmpty) networkAcc
      else removeAux(friends.tail, unfriend(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)
    unfriended - person
  }

  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Mary")

  println(network)  //>> Map(Bob -> Set(), Mary -> Set())
  println(friend(network, "Bob", "Mary")) //>> Map(Bob -> Set(Mary), Mary -> Set(Bob))
  println(unfriend(friend(network, "Bob", "Mary"), "Bob", "Mary"))  //>> Map(Bob -> Set(), Mary -> Set())
  println(remove(friend(network, "Bob", "Mary"), "Bob"))  //>> Map(Mary -> Set())

  // Jim, Bob, Mary
  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(jimBob, "Bob", "Mary")

  println(testNet)  //>> Map(Bob -> Set(Jim, Mary), Mary -> Set(Bob), Jim -> Set(Bob))

  def nFriends(network: Map[String, Set[String]], person: String): Int =
    if (! network.contains(person)) 0
    else network(person).size

  println(nFriends(testNet, "Bob")) //>> 2

  def mostFriends(network: Map[String, Set[String]]): String =
    network.maxBy(pair => pair._2.size)._1

  println(mostFriends(testNet)) //>> Bob

  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int =
    network.filterKeys(k => network(k).isEmpty).size

  def nPeopleWithNoFriendsEqua(network: Map[String, Set[String]]): Int =
    network.count(pair => pair._2.isEmpty)

  println(nPeopleWithNoFriends(testNet))  //>> 0

  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {
    def bfs(target: String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean =
      if (discoveredPeople.isEmpty) false
      else
        val person = discoveredPeople.head
        if (person == target) true
        else if (consideredPeople.contains(person)) bfs(target, consideredPeople, discoveredPeople.tail)
        else bfs(target, consideredPeople + person, discoveredPeople.tail ++ network(person))

    bfs(b, Set(), network(a) + a)
  }

  println(socialConnection(testNet, "Mary", "Jim")) //>> true
  println(socialConnection(network, "Mary", "Bob")) //>> false
}
