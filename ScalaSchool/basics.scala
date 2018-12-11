def addOne(x:Int):Int = {x+1}
//Anonymous functions
(x:Int) => {x+1}
//Partial applications
def adder(x:Int, y:Int):Int = {x+y}
def add2 = adder(x,_:Int)
//Curried fuctions

def multiply(m:Int)(n:Int): Int = m*n
val timesTwo = multiply(2)_
val curriedAdd = (adder _).curried
val addTwo = curriedAdd(2)
addTwo(4)
//variable length arguments
def capitalizeAll(args:String*) = {
 args.map(arg=> arg.capitalize)
}
//Classes
class Calculator{
 val brand = "HP"
 def add(m:Int, n:Int): Int = m + n
}
val calc = new Calculator
calc.add(1,2)
//Expressions
class C {
 var acc = 0
 def minc = { acc+1 }
 val finc = {() => acc +=1}
}
//Inheritance

class ScientificCalculator(brand: String) extends Calculator(brand) {
 def log(m:Double, base:Double) = math.log(m)/math.log(base)
}
//Overloading methods
class EvenMoreScientificCalculator(brand: String) extends ScientificCalculator(brand){def log(m:Int):Double=log(m, math.exp(1))}

//Abstract classes
abstract class Shape{
def getArea():Int
}
class Circle(r:Int) extends Shape {
def getArea():Int = {r*r*3}
}

//Traits
trait Car{
 val brand: String
}
trait Shiny{
 val shineRefraction: Int
}
class BMW extends Car{
 val brand = "BMW"
}
class BMW extends Car {
 val brand = "BMW"	
 val shineRefraction = 12
}

//Types
trait cache[K, V]{
 def get(key:K):V
 def put(key:K, value:V)
 def delete(key:K)
}

def remove[K](key:K)

//apply Methods
class Foo{}

//Objects
/*Objects are used to hold single instance of a class*/

object FooMaker{
 def apply() = new Foo
}
val newFoo = FooMaker()
class Bar{
 def apply() = 0
}

object Timer{
 var count = 0
 def currentCount(): Long = {
  count += 1
  count
 }
}


//Functions are Objects
object addOne extends Function1[Int,Int]{
 def apply(m:Int):Int = m+1
}

class AddOne extends Function1[Int, Int]{
 def apply(m: Int): Int = m + 1
}
val plusOne = new AddOne()
plusOne(1)

//A nice shorthand of extending Function1[Int, Int] is extends (Int => Int)
class AddOne extends (Int => Int) {
 def apply(m: Int): Int = m + 1	
}

//Packages
package com.dipodaimary.example
object colorHolder{
 val BLUE = "Blue"
 val RED = "red"
}
println("the color is: " + com.dipodaimary.example.colorHolder.BLUE)

//Pattern matching
val times = 1
times match {
 case 1 => "one"
 case 2 => "two"
 case _ => "some other number"
}

//match with guards
times match {
 case i if i == 1 => "one"
 case i if i == 2 => "two"
 case _ => "some other number"
}

def bigger(o: Any): Any = {
 o match {
  case i: Int if i < 0 => i - 1
  case i: Int => i + 1
  case d: Double if d < 0.0 => d - 0.1
  case d: Double => d + 0.1
  case text: String => text + "s"
 }
}

//Matching on class members

case class Calculator(brand: String, model: String)

val hp20b = Calculator("HP", "20b")

def calcType(calc: Calculator) = calc match {
 case _ if calc.brand == "HP" && calc.model == "20B" => "financial"
 case _ if calc.brand == "HP" && calc.model == "48G" => "scientific"
 case _ if calc.brand == "HP" && calc.model == "30B" => "business"
 case _ => "unknown"
}



