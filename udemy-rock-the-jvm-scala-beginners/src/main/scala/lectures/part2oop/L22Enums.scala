package lectures.part2oop

object L22Enums extends App {

  // Prior to scala 3, enum is a headache to create, now scala 3 has first class support for enum
  // Enums are a data type similar to a class
  // Sealed data type, cannot be extended
  enum Permissions {
    // you can enumerate all the possible cases (instances) of that type
    case READ, WRITE, EXECUTE, NONE

    // you can add fields/methods
    def openDocument(): Unit =
      if (this == READ) println("opening document...")
      else println("reading not allowed")
  }

  val somePermissions: Permissions = Permissions.READ
  somePermissions.openDocument()  //>> opening document...

  // Enums also take constructor arguments
  enum PermissionWithBits(bits: Int) {
    case READ extends PermissionWithBits(4) // 100
    case WRITE extends PermissionWithBits(2) // 010
    case EXECUTE extends PermissionWithBits(1) // 001
    case NONE extends PermissionWithBits(0) // 000
  }

  /*
    The above code is very boilerplate considering Scala is a compact language, but way better than Scala 2.
    In Java:

    enum Thing {
      case A(2), B(3)
    }
   */

  // You can have companion object
  object PermissionWithBits {
    // factory method
    def fromBits(bits: Int): PermissionWithBits = // whatever
      PermissionWithBits.NONE
  }

  // Standard API baked in Enums
  val somePermissionsOrdinal = somePermissions.ordinal
  println(somePermissionsOrdinal) //>> 0 //because index start at 0

  val allPermissions = PermissionWithBits.values  // array of all possible values of the enum
  val readPermission: Permissions = Permissions.valueOf("READ") // Permissions.READ
}
