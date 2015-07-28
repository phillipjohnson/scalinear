package com.letstalkdata.scalinear

import scala.reflect.ClassTag

object Matrix {

  /**
   * Creates a new Matrix from the specified Vectors
   * @param values the Vectors which will become the rows of the Matrix
   * @tparam T the type of object contained in the Vectors
   * @return the new Matrix
   */
  def apply[T](values:Vector[T]*) = {
    if(values.length > 0) {
      require(values.forall(vector => vector.length == values(0).length),
        "All rows of a matrix must have the same number of elements.")
    }
    new Matrix(values.toArray)
  }

  /**
   * Creates an identity matrix of size `n`
   * @param n the size of the matrix
   * @return an identity matrix of size `n`
   */
  def eye[T:ClassTag](n:Int)(implicit num:Numeric[T]):Matrix[T] = {
    val vectors:Seq[Vector[T]] = for {
      col <- 0 until n
      tabulated = Array.tabulate(n)(f => if(f == col) num.one else num.zero)
     } yield Vector(tabulated:_*)

    new Matrix(vectors.toArray)
  }

  def ones[T:ClassTag](n:Int)(implicit num:Numeric[T]):Matrix[T] = {
    val vectors = for {
      col <- 0 until n
      tabulated = Array.tabulate(n)(_ => num.one)
    } yield Vector(tabulated:_*)

    new Matrix(vectors.toArray)
  }

  def zeros[T:ClassTag](n:Int)(implicit num:Numeric[T]):Matrix[T] = {
    val vectors = for {
      col <- 0 until n
      tabulated = Array.tabulate(n)(_ => num.zero)
    } yield Vector(tabulated:_*)

    new Matrix(vectors.toArray)
  }

  def zeros[T:ClassTag:Numeric](rows:Int, cols:Int)(implicit num:Numeric[T]):Matrix[T] = {
    val vectors = for {
      col <- 0 until rows
      tabulated = Array.tabulate(cols)(_ => num.zero)
    } yield Vector[T](tabulated:_*)

    new Matrix(vectors.toArray)
  }
}

/**
 * A two-dimensional matrix containing one or more Vectors of type T.
 *
 * Matrices are row-major
 *
 * Author: Phillip Johnson
 * Date: 7/6/15
 */
class Matrix[T] private(private val values:Array[Vector[T]]) {

  /**
   * Returns the object at the specified position in the Matrix.
   * @param row the zero-indexed row where the object is located
   * @param col the zero-indexed column where the object is located
   * @return the object at the specified position in the Matrix.
   */
  def apply(row:Int, col:Int):T = values(row).apply(col)

  /**
   * Returns the number of rows in the matrix
   * @return the number of rows in the matrix
   */
  def rows = values.length

  /**
   * Returns the number of columns in the matrix
   * @return the number of columns in the matrix
   */
  def cols = if(rows > 0) values(0).length else 0

  /**
   * Returns the matrix as an array of row Vectors
   * @return the matrix as an array of row Vectors
   */
  def asArray:Array[Vector[T]] = values


  /**
   * Returns the sum of this Matrix and another Matrix
   * @param that the Matrix to add
   * @return the sum of this Matrix and another Matrix
   */
  def +[S >: T:ClassTag](that:Matrix[T])(implicit num:Numeric[S]):Matrix[S] = {
    require(this.rows == that.rows && this.cols == that.cols,
      "Matrices must have the same dimensions to add them.")

    //We cannot delegate to the Vector class because the ClassTag gets lost
    new Matrix(this.values zip that.values map(pair =>
      Vector((pair._1.asArray zip pair._2.asArray).map(numbers =>
        num.plus(numbers._1, numbers._2)):_*)))
  }

  /**
   * Returns the sum of this Matrix's elements and a given number
   * @param n the Matrix to add
   * @return the sum of this Matrix's elements and a given number
   */
  def +[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Matrix[S] = {
    new Matrix(values.map(row =>
      Vector(row.asArray.map(v =>
        num.plus(v, n)):_*)))
  }

  /**
   * Returns the difference of this Matrix and another Matrix
   * @param that the Matrix to subtract
   * @return the difference of this Matrix and another Matrix
   */
  def -[S >: T:ClassTag](that:Matrix[T])(implicit num:Numeric[S]):Matrix[S] = {
    require(this.rows == that.rows && this.cols == that.cols,
      "Matrices must have the same dimensions to subtract them.")

    //We cannot delegate to the Vector class because the ClassTag gets lost
    new Matrix(this.values zip that.values map(pair =>
      Vector((pair._1.asArray zip pair._2.asArray).map(numbers =>
        num.minus(numbers._1, numbers._2)):_*)))
  }

  /**
   * Returns the difference of this Matrix's elements and a given number
   * @param n the Matrix to subtract
   * @return the difference of this Matrix's elements and a given number
   */
  def -[S >: T:ClassTag](n:T)(implicit num:Numeric[S]):Matrix[S] = {
    new Matrix(values.map(row =>
      Vector(row.asArray.map(v =>
        num.minus(v, n)):_*)))
  }

  /**
   * Returns a copy of this Matrix scaled by a magnitude `r`
   * @param r the amount to scale by
   * @return a copy of this Matrix scaled by a magnitude `r`
   */
  def scaleBy[S >: T:ClassTag](r:S)(implicit num:Numeric[S]):Matrix[S] = {
    new Matrix(values map (v => v.scaleBy(r)))
  }

  /**
   * Returns the product of this Matrix and another Matrix.
   * @param that the Matrix to multiply by
   * @return the product of this Matrix and another Matrix.
   */
  def *[S >: T:ClassTag](that:Matrix[S])(implicit num:Numeric[S]):Matrix[S] = {
    require(this.cols == that.rows, "The number of columns in Matrix A must equal the number of rows in Matrix B.")

    val result = Matrix.zeros(rows)

    for(
      (rowVec, row) <- this.values.zipWithIndex;
      (colVec, col) <- that.t().values.zipWithIndex) {
        val q = rowVec.asArray zip colVec.asArray map Function.tupled(num.times(_,_)) reduceLeft num.plus
        result.update(row, col, q)
    }

    result
  }

  /**
   * Returns a new array with the columns and rows transposed
   * @return a new array with the columns and rows transposed
   */
  def t[S >: T:ClassTag]()(implicit num:Numeric[S]):Matrix[S] = {
    val result:Matrix[S] = Matrix.zeros[S](cols, rows)

    for(
      (vector, row) <- values.zipWithIndex;
      (value, col) <- vector.asArray.zipWithIndex) {
      result.update(col, row, value)
    }

    result
  }

  def flatten[S >: T:ClassTag]:Vector[S] = {
    values.foldLeft(Vector[S]())((b, a) => Vector(b.asArray ++ a.asArray:_*))
  }

  /**
   * Updates the value in-place at a given row and column.
   * @param row the row of the item to update
   * @param col the column of the item to update
   * @param value the new value
   */
  def update(row:Int, col:Int, value:T):Unit = {
    values(row).update(col, value)
  }

  override def toString:String = {
    values map(vector => vector.toString) mkString "\n"
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Matrix[T]]

  override def equals(other: Any): Boolean = other match {
    case that: Matrix[T] =>
      (that canEqual this) &&
        values.sameElements(that.values)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(values)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
