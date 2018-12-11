/*
 * Basic datastructures:
 * 1. Arrays
 * 2. Lists
 * 3. Sets
 * 4. Tuples
 * 5. Maps
 * 6. Option
 * 
 * Functional Combinators:
 * 1. map
 * 2. foreach
 * 3. filter
 * 4. zip
 * 5. partition
 * 6. find
 * 7. drop and dropWhile
 * 8. foldRight and foldLeft
 * 9. flatten
 * 10. flatMap
 * 11. Generalized functional combinators
 * 12. Map?
 * */

//Arrays preserve order, can contain duplicates, and are mutable
val numbers = Array(1,2,3,4,5,1,2,3,4,5,6)
numbers(2) = 122

//Lists preserve order, can contain duplicates, and are immutable
val numbers = List(1,2,3,4,5,6,1,2,3,4,5)

//Sets do not preserve order and have no duplicates
val numbers = Set(1,2,3,4,5,6,1,2,3,4,5)

//Tuple: a tuple groups together simple and logical collections of items without using a class
val hostPort = ("localhost",80)
/*Unlike case classes, they don't have name accessors, instead they have accessors that are named by their
 * position and is 1-based rather than 0-based
 * */
hostPort._1
hostPort._2

/*Tuples fit with pattern matching nicely
 * */
 
hostPort match{
 case ("localhost", 80) => println("true") 	
 case ("localhost", 8080) => println("false")
}

/*
 * Tuple has some special sauce for simple making Tuples of 2 values: "->"*/ 
val hostPort = "localhost"->80




//Maps
/*It can hold basic datatypes*/
Map(1->2)
Map("foo"->"bar")
/*Map(1->"one", 2->"two") expands to Map((1,"one"),(2,"two"))*/
/*Map can themselves contain Mpas or even functions as values*/
Map(1->Map("foo"->"bar"))
Map("timesTwo"-> {timesTwo(_)})


//Option is a container that may or may not hold something
/*The basic interface for Option looks like*/
trait Option[T]{
 def isDefined: Boolean
 def get: T
 def getOrElse(t:T): T
}
/*Option itself is generic and has two subclass: Some[T] or None
 * Let's look at an example of how Option is used:*/
val numbers = Map("one"->1, "two"->2)
numbers.get("two")
numbers.get("three")

/*Now our data appears trapped in this Option. How do we work with it?
 * A first instinct might be to do something conditionally based on the isDefined method
 * */
//We want to multiply the number by two, otherwise return 0
val result = if(res43.isDefined){
 res43.get*2
}else{
 0
}

//It is recommended that we use getOrElse or pattern matching to work with this result
val result = res42.getOrElse(0)*2
/*
 * Patter matching fits naturally with Option
 * */
val result = res42 match {
 case Some(n) => n*2
 case None => 0
}

/*********************************************************************************************************/

/**FUNCTIONAL COMBINATORS**/

//map: evaluates a function over each element in the list, returning a list with the same number of elements 
List(1,2,3).map(x=>x*x)
List(1,2,3).map((x:Int)=>x*x)
def timesTwo(i:Int):Int = i*2
List(1,2,3).map(timesTwo)

//foreach is like a map but returns nothing. foreach is intended for side-effects only
val numbers = List(1,2,3,4,5)
numbers.foreach(timesTwo)


//filter: removes any element where the function you pass in evaluates to false. 
 /* Functions that return a Boolean are often called predicate functions.
 * */

numbers.filter((i:Int) => i%2 == 0)
def isEven(x:Int):Boolean = x%2==0
numbers.filter(isEven)

//zip: aggregates the contents of two lists into single list of pairs
List(1,2,3,4).zip(List("a","b","c","d"))

//partition: splits a list based on where it falls with respect to a predicate function
val numbers = List(1,2,3,4,5,6,7,8,9,10,11,12,13)
numbers.partition(isEven)


//find: find returns the first element of a collection that matches a predicate function
numbers.find(x=>x>6)

//drop and dropWhile:
/*drop: drops the first i elements*/
numbers.drop(5)
/*dropWhile removes the first element that match a predicate function.
 * For example, if we dropWhile odd numbers from out list of numbers, 1 gets dropped (but not 3 which is "shielded" by 2)*/
numbers.dropWhile(_% 2 !=0)

//foldLeft:
numbers.foldLeft(0)((m:Int,n:Int)=>m+n) //0 is the starting value and m acts as an accumulator

/*Seen visually*/
numbers.foldLeft(0){(m:Int,n:Int)=>println("m: " + m + ", n: " + n); m+n}
/*
m: 0, n: 1
m: 1, n: 2
m: 3, n: 3
m: 6, n: 4
m: 10, n: 5
m: 15, n: 6
m: 21, n: 7
m: 28, n: 8
m: 36, n: 9
m: 45, n: 10
m: 55, n: 11
m: 66, n: 12
m: 78, n: 13
res67: Int = 91
*/

//foldRight: is same as foldLeft except it runs in the opposite direction
numbers.foldRight(0){(m:Int,n:Int)=>println("m: " + m + ", n: " + n); m+n}
/*
m: 13, n: 0
m: 12, n: 13
m: 11, n: 25
m: 10, n: 36
m: 9, n: 46
m: 8, n: 55
m: 7, n: 63
m: 6, n: 70
m: 5, n: 76
m: 4, n: 81
m: 3, n: 85
m: 2, n: 88
m: 1, n: 90
res69: Int = 91
*/



//flatten: collapses one level of nested structure
List(List(1,2), List(2,4)).flatten
Set(Set(1,2),Set(2,3)).flatten



//Generalized functional combinators
/*
 * Now we've learned a grab-bag of functions for working with collections.
 * What we'd like is to be able to write our own functional combinators.
 * Interestingly, every functional combinator shown above can be written on top of fold. Let's see some examples.
 * */

def ourMap(numbers:List[Int], fn: Int=>Int): List[Int] = {
 numbers.foldRight(List[Int]()){(x:Int, xs:List[Int])=> fn(x) :: xs}
}


def ourMap2(numbers:List[Int], fn: Int=>Int): List[Int] = {
 numbers.foldRight(List[Int]()){ (x:Int, xs:List[Int]) => fn(x) :: xs
 }
}


//Will this functional combinators work on Maps?
/*All of the functional combinators shown work on Maps too. Maps can be thought of as a list of pairs so the functions
 * you write work on a pair of the keys and values in the Map*/
val extensions = Map("steve"->100,"bob"->101,"joe"->201)
extensions.filter((namePhone:(String,Int)) => namePhone._2<200)
/*Because it gives you a tuple, you have to pull out the keys and values with their positional accessors. Yuck!*/
/*Lucky us, we can actually use a pattern match to extract the key and value nicely*/
extensions.filter({case (name, extension) => extension<200})
