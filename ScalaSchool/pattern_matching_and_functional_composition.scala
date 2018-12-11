//Functional Composition
/*Let's make two aptly named functions:*/
def f(s:String) = "f(" + s + ")"
def g(s:String) = "g(" + s + ")"

//compose: compose makes a new function that composes other functions f(g(x))*/
val fComposeG = f _ compose g _


//andThen: is like compose, but calls the first function and then the second, g(f(x))
val fAndThenG = f _ andThen g _


//Currying and Partial Application

//case statements
/*
 * So just what are case statements?
 * It's a subclass of function called a PartialFunction
 * What is a collection of multiple case statements?
 * They are multiple PartialFunctions composed together.
 * */

/**Understanding PartialFunction**/
/*A function works for every argument of the defined type. In other words, a function defined as (Int)=>String takes any
 * Int and returns a String. A PartialFunction is only defined for certain values of the defined type. A Partial Function
 * (Int) => String might not accept every Int.
 * isDefinedAt is a method on PartialFunction that can be used to determine if the PartialFunction will accept a given
 * argument.
 * Note: PartialFunction is unrelated to a partially applied function that we talked about earlier.
 * */
val one: PartialFunction[Int, String] = {case 1 => "one"}
one.isDefinedAt(1)
one.isDefinedAt(2)
/*We can apply a partial function*/
one(1)

/*PartialFunctions can be composed with something new called orElse, that reflects whether the PartialFunction is defined
 * over the supplied argument.*/
val two: PartialFunction[Int, String] = {case 2 => "two"}
val three: PartialFunction[Int, String] = {case 3 => "three"}
val wildcard: PartialFunction[Int, String] = {case _ => "something else"}
val partial = one orElse two orElse three orElse wildcard

/**The mystery of case**/
/*A case statement used where a function is used normally*/
case class PhoneExt(name:String, ext:Int)
val extensions = List(PhoneExt("steve",100),PhoneExt("robey",200))
extensions.filter{case PhoneExt(name, extension) => extension<200}

/*Why does this work?
 * filter takes a function. In this case a predicate function of(PhoneExt)=>Boolean
 * A PartialFunction is a subtype of Function so filter can also take a PartialFunction!
 * */
