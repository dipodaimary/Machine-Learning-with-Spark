/**Type and Polymorphism basics**/

//What are static types? Why are they useful?
/*
 * According to Pierce: "A type system is a systematic method for automatically checking the absence of certain
 * erroneous behaviours by classifying program phrases according to the kinds of values they compute."
 * 
 * Type allow you to denote function domain & codomains. For example, from mathematics, we are used to seeing:
 * 
 * f: R -> N
 * this tells us that function "f" maps values from the set real numbers to values of the set of natural numbers.
 * 
 * In the abstract, this is exactly what concrete types are. Type systems give us some more powerful ways to express
 * these sets.
 * 
 * Given these annotations, the compiler can now statically (at compile time) verify that the program is sound. That is,
 * compilation will fail if values (at runtime) will not comply to the constraint imposed by the program.
 * 
 * Generally speaking, the typechecker can only guarantee that unsound programs do not compile. It cannot guarantee that
 * every soud program will compile
 * 
 * With increasing expressiveness in type systems, we can produce more reliable code because it allows us to prove
 * invariants about our program before it even runs (modulo bugs in the types themselves, of course!). Academia is
 * pushing the limits of expressiveness very hard, including value-dependent types!
 * 
 * Note that all type information is removed at compile time. It is no longer needed. This is called erasure.
 * */


//Types in Scala
/*
 * Scala's powerful type system allows for very rich expression. Some of its chief features are:
 * 1. parametric polymorphism: roughly, generic programming
 * 2. (local) type inference: roughly, why you needn't say val i: Int = 12: Int
 * 3. existential quantification: roughly, defining something for some unnames type
 * 4. views: we'll learn these next week; roughly, "castability" of values of one type to another
 * */


//Parametric polymorphism
/*
 * Polymorphism is used in order to write generic code (for values of different types) without compromising static
 * typing richness.
 * 
 * For example, without parametric polymorphism, a generic list data structure would always look like this (and indeed
 * it did look like this in Java prior to generics):
 * 2 :: 1 :: "bar" :: "foo" :: Nil
 * Now we cannot recover any type information about the individual members. And so our application would devolve into a series
 * of casts ("asInstanceOf[]") and we would lack type safety (because these are all dynamic)
 * 
 * Polymorphism is achieved through specifying type variables.
 * 
 * def drop1[A](l: List[A]) = l.tail
 * drop1(List(1,2,3))
 * */


//Scala has rank-1 polymorphism
/*Roughly, this means that there are some type concepts you'd like to express in Scala that are
 * "too generic" for the compiler to understand. Suppose you had some function
 * def toList[A](a:A) = List(a)
 * which you wished to use generically:
 * def foo[A,B](f:A=>List[A], b:B) = f(b)
 * This does not compile, because all type variables have to be fixed at the invocation site. Even if you "nail down" type B
 * def foo[A](f:A=> List[A], i:Int) = f(i)
 * ... you get a type mismatch
 * */

//Type inference
/*
 * A traditional objection to static typing is that it has much syntatic overhead. Scala alleviates this by providing type inference.
 * The classic method for type inference in functional programming languages is Hindley-Milner, and it was first employed in ML.
 * Scala's type inference system works a little differently, but it's similar in spirit: infer constraints, and attempt to unify a type.
 * In scala, for example, you cannot do the following:
 * {x => x}
 * 
 * In scala all type inference is local. Scala considers one expression at a time. For example:
 * def id[T](x:T) = x
 * val x = id(322)
 * val x = id("hey")
 * val x = id(Array(1,2,3,4))
 * They are now preserved, the scala compiler infers the type parameter for us. Note also how we did not have to
 * specify the return type explicitly.
 * */

//Variance:
/*
 * Scala's type system has to account for class hierarchies together with polymorphism. Class hierarchies allow the expression of subtype
 * relationships. A central question that comes up when mixing OO with polymorphism is: if T' is a subclass of T, is Container[T'] considered
 * a subclass of Container[T]? Variance annotations allow you to express the following relationships between class hierarchies & polymophic
 * types:
 * 					Meaning							Scala notation
 * covariant		C[T'] is a subclass of C[T]		[+T]
 * contravariant 	C[T] is a subclass of C[T']		[-T]
 * invariant		C[T] and C[T'] are not related	[T]
 * 
 * The subtype relationship really means: for a given type T, if T' is a subtype, can you substitute it?
 * */

class Covariant[+A]
val cv: Covariant[AnyRef] = new Covariant[String]
val cv: Covariant[String] = new Covariant[AnyRef]

class Contravariant[-A]
val cv: Contravariant[String] = new Contravariant[AnyRef]
val fail: Contravariant[AnyRef] = new Contravariant[String]

/*Contravariant seems strange. When it is used? Somewhat surprising!*/
trait Function1 [-T1, +R] extends AnyRef
/*if you think about this from the point of view of substitution, it makes a lot of sense. Let's first define a simple class
 * hierarchy:
 * */
class Animal {val sound = "rustle"}
class Bird extends Animal {override val sound = "call"}
class Chicken extends Bird {override val sound = "cluck"}

/*Suppose you need a function that takes a Bird param*/
val getTweet: (Bird => String) = //TODO
/*The standard animal library has a function that does what you want, but it takes an Animal parameter instead.
 * In most sutuations, if you say "I need a __. I have a subclass of __", you're OK. But function parameters are
 * contravariant. If you need a function that takes a Bird and you have a function that takes a Chicken, that
 * function would choke on a Duck. But a function that takes an Animal is OK:
 * */
val hatch: (() => Bird) = (() => new Chicken )


//Bounds:
/*Scala allows you to restrict polymorphic variables using bounds. These bounds express subtype relationships.
 * def cacophony[T](things: Seq[T]) = things map (_.sound)
 * */
def biophony[T <: Animal](things: Seq[T]) = things map (_.sound)
biophony(Seq(new Chicken, new Bird))
/*Lower type bounds are also supported; they come in handy with contravariance and clever covariance. List[+T] is covariant; a list
 * of Birds is a list of Animals. List defines an operator :: (elem T) that returns a new List with elem prepended. The new List
 * has the same type as the original:*/
val flock = List(new Bird, new Bird)
new Chicken :: flock

/*List also defines ::[B>T](x:B) which returns a List[B]. Notice the B>:T. That specifies type B as a superclass of T.
 * That let us do the right thing when prepending an Animal to a List[Bird]:*/
new Animal :: flock

//Quantification:
/*Sometime you do not care to be able to name a type variable, for example:
 */
def count[A](l:List[A]) = l.size
/*Instead you can use wildcards*/
def count(l:List[_]) = l.size
/*This is shorthand for:*/
def count(l: List[T forSome {type T}]) = l.size
/*Note that quantification can get tricky*/
def drop1[l:List[_]) = l.tail
/*Suddenly we lost type information! To see what's going on, revert to the heavy-handed syntax:*/
def drop1(l:List[T forSome {type T}]) = l.tail
/*We can't say anything about T because the type does not allow it.*/
def hashcodes(l:Seq[_<: AnyRef]) = l map (_.hashCode)
hashcodes(Seq("one", "two", "three"))






