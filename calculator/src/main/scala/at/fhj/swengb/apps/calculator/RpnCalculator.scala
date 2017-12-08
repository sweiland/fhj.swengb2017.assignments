package at.fhj.swengb.apps.calculator

import scala.util.Try

/**
  * Companion object for our reverse polish notation calculator.
  */
object RpnCalculator {

  /**
    * Returns empty RpnCalculator if string is empty, otherwise pushes all operations
    * on the stack of the empty RpnCalculator.
    *
    * @param s a string representing a calculation, for example '1 2 +'
    * @return
    */
  def apply(s: String): Try[RpnCalculator] = if (s.isEmpty) Try(RpnCalculator()) else {
    val stack: List[Op] = s.split(' ').map(e => Op(e)).toList
    var calc: Try[RpnCalculator] = Try(RpnCalculator())
    stack.foreach(e => calc = calc.get.push(e))
    calc
  }

}

/**
  * Reverse Polish Notation Calculator.
  *
  * @param stack a datastructure holding all operations
  */
case class RpnCalculator(stack: List[Op] = Nil) {

  /**
    * By pushing Op on the stack, the Op is potentially executed. If it is a Val, the op instance is just put on the
    * stack, if not the stack is examined and the correct operation is performed.
    *
    * @param op
    * @return
    */
  def push(op: Op): Try[RpnCalculator] = {
    if (stack.isEmpty) Try(RpnCalculator())
    if (op.equals(Val)) {
      var nStack = stack :+ op
      Try(RpnCalculator(nStack))
    }
  }
    /**
      * Pushes val's on the stack.
      *
      * If op is not a val, pop two numbers from the stack and apply the operation.
      *
      * @param op
      * @return
      */
    def push(op: Seq[Op]): Try[RpnCalculator] = ???

    /**
      * Returns an tuple of Op and a RevPolCal instance with the remainder of the stack.
      *
      * @return
      */
    def pop(): (Op, RpnCalculator) = ???

    /**
      * If stack is nonempty, returns the top of the stack. If it is empty, this function throws a NoSuchElementException.
      *
      * @return
      */
    def peek(): Op = ???

    /**
      * returns the size of the stack.
      *
      * @return
      */
    def size: Int = ???
  }