package typetag

import scala.reflect.runtime.universe._
import scala.reflect._

object  RuntimeTypeInformation {
  // To overcome Type erasure, or, how to obtain type information of variables at run time:
  // 1. TypeTag, all type info at runtime
  // 2. ClassTag, the runtime class of the type, but doesn't inform us about the type info
  // 3. WeakTypeTag, a weaker version of TypeTag, abstract type info

  def main(args: Array[String]): Unit = {
    TypeTag.main(args)
  }

  object TypeTag {
    // Strongest, most detailed way to obtain type info at runtime
    // If we instruct the compiler to find an implicit of TypeTag[T] --> easy obtain the type info
    def obtainTypeTag[T](implicit tt: TypeTag[T]) = tt
    def main(args: Array[String]): Unit = {
      // assert(obtainTypeTag[Int].tpe == Int)
      println(obtainTypeTag[Int].tpe) // Int
      println(obtainTypeTag[List[Map[Int, (Long, String)]]])
      println(checkType(List("foo", "bar")))
    }
    // redo the method from TypeErasure
    def checkType[T: TypeTag](v: T) = typeOf[T] match {
      case t if t =:= typeOf[List[String]] => "List of Strings"
      case t if t =:= typeOf[List[Int]] => "List of Ints"
    }
  }

  object ClassTag {
    // Assume we want to write a function creating a List of elements having the same type
    def makeListFrom[T](elems: T*): List[T] = List[T](elems: _*)
    // But we cannot do the same for Array
    // def makeArrayFrom[T](elems: T*): Array[T] = Array[T](elems: _*)
    def makeArrayFrom[T: ClassTag](elems: T*): Array[T] = Array[T](elems: _*)
    // For List, Type Erasure will erase List[T] to List..
    // For Array, Array[T] is not erased
  }

  object WeakTypeTag {
    // All of the types have been concrete
    // If the type is abstract --> CANNOT USE TypeTag
    trait Foo {
      type Bar
      // def barType = typeTag[Bar].tpe
      def barType = weakTypeTag[Bar].tpe
    }
    // ^^^, here, trait Foo has an abstract type: Bar
    // barType exposes the reflective representation of type Bar..
    // .. but not compile because compiler cannot instantiate proper TypeTag for abstract type Bar
  }
}
