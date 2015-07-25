package com.letstalkdata.scalinear

import scala.reflect.ClassTag


object Vector {
  /**
   * Creates a new Vector that contains the supplied values.
   * @param values the values use to fill the Vector.
   * @tparam T the type of object to be stored in the Vector.
   * @return a new Vector that contains the supplied values.
   */
  def apply[T:ClassTag](values:T*) = {
    new Vector(values.toArray)
  }

  /**
   * Creates a new Vector padded with zeros.
   * @param length the number of zeros to include
   * @tparam T the numeric type that 0 should be cast to
   * @return a new Vector padded with zeros
   */
  def zeros[T:ClassTag](length:Int)(implicit num:Numeric[T]):Vector[T] = {
    new Vector(Array.fill(length) { num.zero })
  }

  /**
   * Creates a new Vector padded with ones.
   * @param length the number of ones to include
   * @tparam T the numeric type that 1 should be cast to
   * @return a new Vector padded with ones
   */
  def ones[T:ClassTag](length:Int)(implicit num:Numeric[T]):Vector[T] = {
    new Vector(Array.fill(length) { num.one })
  }
}

/**
 * A one-dimensional matrix that contains items of a given type T.
 *
 * Although any type of object can be stored in a Vector, numbers make the
 * most sense.
 *
 * Author: Phillip Johnson
 * Date: 7/6/15
 */
class Vector[T] private(private val values:Array[T]) {

  /**
   * Returns the item at the given index.
   * @param index the position whose value will be retrieved.
   * @return the item at the given index.
   */
  def apply(index:Int):T = values(index)

  /**
   * Returns the number of items in the Vector.
   * @return the number of items in the Vector.
   */
  def length:Int = values.length

  /**
   * Returns the Vector as an Array
   * @return the Vector as an Array
   */
  def asArray:Array[T] = values

  /**
   * Returns `true` if this Vector is opposite to the given Vector else `false`.
   *
   * The Vector <em>u</em> is opposite to the Vector <em>v</em> if <em>u</em><sub>1</sub> = -<em>v</em><sub>1</sub>, <em>u</em><sub>2</sub> = -<em>v</em><sub>2</sub>, ..., <em>u</em><sub>n</sub> = -<em>v</em><sub>n</sub>
   *
   * @param that the Vector to compare to this Vector.
   * @return `true` if this Vector is opposite to the given Vector else `false`.
   */
  def isOpposite[S >: T](that:Vector[T])(implicit num:Numeric[S]):Boolean = {
    val thisVals = this.values.zipWithIndex
    this.length == that.length &&
      thisVals.forall(p => p._1 == num.negate(that(p._2)))
  }

  /**
   * Returns the sum of this Vector and a given Vector.
   *
   * This operation adds the elements at each index.
   * For example: `Vector(1,2,3) + Vector(2,2,2)` yields `Vector(3,4,5)`
   *
   * @param that the Vector to add
   * @return the sum of this Vector and a given Vector.
   */
  def +[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):Vector[S] = {
    require(this.length == that.length, "Vectors must be of same length.")
    val added:Array[S] = this.values.zip(that.values).map(p => num.plus(p._1, p._2))
    new Vector(added)
  }

  /**
   * Returns the sum of this Vector's elements and a given number.
   *
   * Adds the given number to every element in the Vector.
   * For example: `Vector(1,2,3) + 2` yields `Vector(3,4,5)`
   *
   * @param n the number to add
   * @return the resulting Vector
   */
  def +[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(this.values.map(m => num.plus(m, n)))
  }

  /**
   * Returns the difference of this Vector and a given Vector.
   *
   * This operation subtracts the elements at each index.
   * For example: `Vector(1,2,3) - Vector(2,2,2)` yields `Vector(-1,0,1)`
   *
   * @param that the Vector to subtract
   * @return the resulting Vector
   */
  def -[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):Vector[S] = {
    require(this.length == that.length, "Vectors must be of same length.")
    val minused:Array[S] = this.values.zip(that.values).map(p => num.minus(p._1, p._2))
    new Vector(minused)
  }

  /**
   * Returns the difference of this Vector's elements and a given number.
   *
   * Subtracts the number from every element in the Vector.
   * For example: `Vector(1,2,3) - 2` yields `Vector(-1,0,1)`
   *
   * @param n the number to subtract
   * @return the difference of this Vector's elements and a given number.
   */
  def -[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(this.values.map(m => num.minus(m, n)))
  }

  /**
   * Returns a copy of this Vector scaled by a magnitude.
   *
   * This operation multiplies every element in the Vector by the given magnitude.
   * For example: `Vector(1,2,3) * 2` yields `Vector(2,4,6)`.
   *
   * @param r the number to scale by
   * @return the resulting Vector
   */
  def scaleBy[S >: T:ClassTag](r:S)(implicit num:Numeric[S]):Vector[S] = {
    new Vector(values.map(n => num.times(n, r)))
  }

  /**
   * Returns the quotient of this Vector's elements and a given number.
   *
   * This operation divides every element in the Vector by the given number. For example
   * `Vector(2.0, 4.0, 6.0) / 2` yields `Vector(1.0, 2.0, 3.0)`
   *
   * @param n the number to divide by
   * @return the resulting Vector
   */
  def /[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Vector[S] = num match {
    case num: Fractional[T] => new Vector(this.values.map(m => num.div(m, n)))
    case num: Integral[T] => new Vector(this.values.map(m => num.quot(m, n)))
    case _ => throw new IllegalArgumentException("Unable to divide type.")
  }

  /**
   * Returns the dot product of the Vectors.
   *
   * Alias for [[dot]].
   *
   * @param that the other Vector to dot product multiply by
   * @return the dot product of the Vectors.
   */
  def ∙[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):S = {
    dot[S](that)
  }

  /**
   * Returns the dot product of the Vectors.
   * @param that the other Vector to dot product multiply by
   * @return the dot product of the Vectors.
   */
  def dot[S >: T:ClassTag](that:Vector[T])(implicit num:Numeric[S]):S = {
    require(this.length == that.length, "Vectors must be of same length.")
    this.values.zip(that.values).map(p => num.times(p._1, p._2)).sum
  }

  /**
   * Updates the value in-place at a given index.
   * @param index the index to update
   * @param value the new value
   */
  def update(index:Int, value:T):Unit = {
    values.update(index, value)
  }

  /**
   * Returns a new Vector with the value appended
   * @param x the value to append
   * @return a new Vector with the value appended
   */
  def append[S >: T:ClassTag](x: T):Vector[S] = {
    new Vector(values :+ x)
  }

  /**
   * Returns a new Vector extended by the supplied Vector
   * @param xs the Vector to append
   * @return a new Vector extended by the supplied Vector
   */
  def extend[S >: T:ClassTag](xs:Vector[T]):Vector[S] = {
    new Vector(values ++ xs.values)
  }


  override def toString = values mkString("[", ", ", "]")

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
