package typetag

// https://www.baeldung.com/scala/type-information-at-runtime
/**
* The compiler removes all generic type information at compile-time, leaving this information missing at runtime.
*/
object TypeErasure {
  // we define 2 list, 1 list of int, 1 list of string
  val intList: List[Int] = List(1, 2, 3)
  val strList: List[String] = List("foo", "bar")

  // define a function that checks the type of a parameter
  def checkType[A](xs: List[A]) = xs match { // .. type A is ERASED ..
    case _: List[String] => "List of Strings" // .. and gives this warning
    case _: List[Int] => "List of Ints"
  }
}
