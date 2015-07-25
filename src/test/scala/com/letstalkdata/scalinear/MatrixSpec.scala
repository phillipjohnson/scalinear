package com.letstalkdata.scalinear

/**
 * Author: Phillip Johnson
 * Date: 7/17/15
 */
class MatrixSpec extends UnitSpec {
  "A Matrix" should "be created using its factory method" in {
    val myMatrix = Matrix(Vector(1,2,3),Vector(4,5,6),Vector(7,8,9))

    myMatrix(0,0) should be(1)
    myMatrix(1,1) should be(5)
    myMatrix(2,2) should be(9)

  }
  it must "have the same number of elements in each row" in {
    intercept[IllegalArgumentException] {
      Matrix(Vector(1,2), Vector(1))
    }
  }
  it can "be created as an identity matrix" in {
    val myMatrix = Matrix.eye[Int](3)
    myMatrix(0,0) should be (1)
    myMatrix(0,1) should be (0)
    myMatrix(0,2) should be (0)
    myMatrix(1,0) should be (0)
    myMatrix(1,1) should be (1)
    myMatrix(1,2) should be (0)
    myMatrix(2,0) should be (0)
    myMatrix(2,1) should be (0)
    myMatrix(2,2) should be (1)
  }
  it should "be typed" in {
    val myIntMat = Matrix.eye[Int](2)
    myIntMat(0,0) should be(1)

    val myFloatMat = Matrix.eye[Float](2)
    myFloatMat(0,0) should be(1.0f)
  }
  it can "be filled with ones" in {
    val myOnes = Matrix.ones[Int](4)
    assert(myOnes.asArray.forall(vector => vector.asArray.forall(n => n == 1)))
  }
  it can "be filled with zeroes" in {
    val myOnes = Matrix.zeros[Double](4)
    assert(myOnes.asArray.forall(vector => vector.asArray.forall(n => n == 0.0)))
  }
  it should "be equal to an identical Matrix" in {
    val one = Matrix[Int](Vector(1,2,3), Vector(4,5,6))
    val two = Matrix[Int](Vector(1,2,3), Vector(4,5,6))

    one should equal(two)
  }
  it should "not be equal to a different Matrix" in {
    val one = Matrix[Int](Vector(1,2,3), Vector(4,5,6))
    val two = Matrix[Int](Vector(0,2,3), Vector(4,5,6))

    one should not equal(two)
  }
  "Matrices" can "be added" in {
    val a:Matrix[Int] = Matrix.eye(3)
    val b:Matrix[Int] = Matrix.ones(3)
    val myMatrix:Matrix[Int] = a + b

    myMatrix(0,0) should be (2)
    myMatrix(0,1) should be (1)
    myMatrix(0,2) should be (1)
    myMatrix(1,0) should be (1)
    myMatrix(1,1) should be (2)
    myMatrix(1,2) should be (1)
    myMatrix(2,0) should be (1)
    myMatrix(2,1) should be (1)
    myMatrix(2,2) should be (2)
  }
  they can "be subtracted" in {
    val a:Matrix[Int] = Matrix.eye(3)
    val b:Matrix[Int] = Matrix.ones(3)
    val myMatrix:Matrix[Int] = a - b

    myMatrix(0,0) should be (0)
    myMatrix(0,1) should be (-1)
    myMatrix(0,2) should be (-1)
    myMatrix(1,0) should be (-1)
    myMatrix(1,1) should be (0)
    myMatrix(1,2) should be (-1)
    myMatrix(2,0) should be (-1)
    myMatrix(2,1) should be (-1)
    myMatrix(2,2) should be (0)
  }
  they can "be scaled" in {
    val myMatrix:Matrix[Int] = Matrix.eye(2)
    val scaled = myMatrix scaleBy 2

    scaled(0,0) should be (2)
    scaled(0,1) should be (0)
    scaled(1,0) should be (0)
    scaled(1,1) should be (2)
  }
  they can "be multiplied" in {
    val A = Matrix(Vector(1,0,-2),Vector(0,3,-1))
    val B = Matrix(Vector(0,3),Vector(-2,-1),Vector(0,4))
    val multiplied = A * B
    multiplied(0,0) should be(0)
    multiplied(0,1) should be(-5)
    multiplied(1,0) should be(-6)
    multiplied(1,1) should be(-7)

  }
  they can "be transposed" in {
    val myMatrix = Matrix(Vector(1,2,3),Vector(4,5,6))
    val transposed = myMatrix.t()

    transposed.rows should be(3)
    transposed.cols should be(2)

    transposed(0,0) should be(1)
    transposed(0,1) should be(4)
    transposed(1,0) should be(2)
    transposed(1,1) should be(5)
    transposed(2,0) should be(3)
    transposed(2,1) should be(6)
  }
  they can "be transposed with types" in {
    val myMatrix = Matrix[Float](Vector(1.0f,2.0f,3.0f),Vector(4.0f,5.0f,6.0f))
    val transposed = myMatrix.t()

    transposed.rows should be(3.0f)
    transposed.cols should be(2.0f)

    transposed(0,0) should be(1.0f)
    transposed(0,1) should be(4.0f)
    transposed(1,0) should be(2.0f)
    transposed(1,1) should be(5.0f)
    transposed(2,0) should be(3.0f)
    transposed(2,1) should be(6.0f)
  }


}
