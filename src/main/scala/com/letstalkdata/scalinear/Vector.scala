package com.letstalkdata.scalinear

import scala.reflect.ClassTag


/**
 * Author: Phillip Johnson
 * Date: 7/6/15
 */
object Vector {
  def apply[T:ClassTag](values:T*) = {
    new Vector(values.toArray)
  }
}

class Vector[T] private(val values:Array[T]) {

  def apply(index:Int):T = values(index)

  def length:Int = values.length

  def asArray:Array[T] = values

  def isOpposite[S >: T](that:Vector[T])(implicit num:Numeric[S]):Boolean = {
    val thisVals = this.values.zipWithIndex
    this.length == that.length &&
      thisVals.forall(p => p._1 == num.negate(that(p._2)))

  }

  def +[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):Vector[S] = {
    require(this.length == that.length, "Vectors must be of same length.")
    val added:Array[S] = this.values.zip(that.values).map(p => num.plus(p._1, p._2))
    new Vector(added)
  }

  def +[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(this.values.map(m => num.plus(m, n)))
  }

  def -[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):Vector[S] = {
    require(this.length == that.length, "Vectors must be of same length.")
    val minused:Array[S] = this.values.zip(that.values).map(p => num.minus(p._1, p._2))
    new Vector(minused)
  }

  def -[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(this.values.map(m => num.minus(m, n)))
  }

  def scaleBy[S >: T:ClassTag](r:S)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(values.map(n => num.times(n, r)))
  }

  def /[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = num match {
    case num: Fractional[T] => new Vector(this.values.map(m => num.div(m, n)))
    case num: Integral[T] => new Vector(this.values.map(m => num.quot(m, n)))
    case _ => throw new IllegalArgumentException("Unable to divide type.")
  }

  def ∙[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):S = {
    dot[S](that)
  }

  def dot[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):S = {
    require(this.length == that.length, "Vectors must be of same length.")
    this.values.zip(that.values).map(p => num.times(p._1, p._2)).sum
  }

  def update(index:Int, value:T):Unit = {
    values.update(index, value)
  }

  def append[S >: T:ClassTag](x: T):Vector[S] = {
    new Vector(values :+ x)
  }

  def extend[S >: T:ClassTag](xs:Vector[T]):Vector[S] = {
    new Vector(values ++ xs.values)
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Vector[T]]

  override def equals(other: Any): Boolean = other match {
    case that: Vector[T] =>
      (that canEqual this) &&
        values.sameElements(that.values)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(values)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
