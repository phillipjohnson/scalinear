package com.letstalkdata.scalinear

/**
 * Author: Phillip Johnson
 * Date: 7/6/15
 */
class VectorSpec extends UnitSpec {
  "A Vector" should "be created from the factory method" in {
    val myVector = Vector(1,2,3)
    myVector(0) should be (1)
    myVector(1) should be (2)
    myVector(2) should be (3)
  }
  it should "have a length equal to the number of elements" in {
    Vector().length should be (0)
    Vector(1).length should be (1)
    Vector(1, 2).length should be (2)
  }
  it should "be equal to an equivalent Vector" in {
    Vector[Int]() should equal(Vector[Int]())
    Vector(1) should equal(Vector(1))
    Vector(1,2,3) should equal(Vector(1,2,3))
  }
  it should "not be equal to non-equivalent Vectors" in {
    Vector(1,2) should not equal Vector()
    Vector(1.0, 2.0) should not equal Vector(new Object(), new Object())
  }
  it can "be element-wise added to" in {
    Vector(1,2,3) + 4 should equal(Vector(5,6,7))
    Vector(1.0, 2.0, 3.0) + 2.0 should equal(Vector(3.0, 4.0, 5.0))
  }
  it can "be element-wise minused" in {
    Vector(1,2,3) - 4 should equal(Vector(-3, -2, -1))
    Vector(1.0, 2.0, 3.0) - 2.0 should equal(Vector(-1.0, 0.0, 1.0))
  }
  it can "be element-wise divided" in {
    Vector(1,2,3) / 4 should equal(Vector(0, 0, 0))
    Vector(1.0f, 2.0f, 3.0f) / 2.0f should equal(Vector(0.5, 1.0, 1.5))
    Vector() / 4 should equal(Vector())
  }
  it can "be appended to" in {
    Vector().append(1) should be (Vector(1))
    Vector(1,2).append(1) should be (Vector(1,2,1))
  }
  it can "be extended" in {
    Vector().extend(Vector(1,2,3)) should be (Vector(1,2,3))
    Vector(4,5,6).extend(Vector(7,8,9)) should be (Vector(4,5,6,7,8,9))
  }
  it can "be updated" in {
    val a = Vector(1,2,3)
    a.update(0, 999)
    a should be(Vector(999,2,3))

    intercept[ArrayIndexOutOfBoundsException] {
      Vector().update(0, 999)
    }
  }

  "Opposite operation" should "be true when the Vector is opposite" in {
      val a = Vector( 1,  2,  3)
      val b = Vector(-1, -2, -3)

      assert(a.isOpposite(b))
  }
  it should "be commutative" in {
    val a = Vector( 1,  2,  3)
    val b = Vector(-1, -2, -3)

    assert(a.isOpposite(b))
    assert(b.isOpposite(a))
  }
  it should "work on all numeric types" in {
    val c = Vector( 1.0f,  2.0f,  3.0f)
    val d = Vector(-1.0f, -2.0f, -3.0f)

    assert(c.isOpposite(d))

    val e = Vector( 1.0d,  2.0d,  3.0d)
    val f = Vector(-1.0d, -2.0d, -3.0d)

    assert(e.isOpposite(f))

    val g = Vector( 1L,  2L,  3L)
    val h = Vector(-1L, -2L, -3L)

    assert(g.isOpposite(h))
  }

  "Vectors" can "be added" in {
    Vector(1,2,3) + Vector(1,1,1) should equal(Vector(2,3,4))
    Vector(1,2,3) + Vector(-1,-1,-1) should equal(Vector(0,1,2))
  }
  they must "be same length to be operated on " in {
    intercept[IllegalArgumentException] {
      Vector(1,2) + Vector(1,2,3)
    }
    intercept[IllegalArgumentException] {
      Vector(1,2) - Vector(1,2,3)
    }
    intercept[IllegalArgumentException] {
      Vector(1,2) ∙ Vector(1,2,3)
    }
  }
  they can "be subtracted" in {
    Vector(1,2,3) - Vector(1,1,1) should equal(Vector(0,1,2))
    Vector(1,2,3) - Vector(-1,-1,-1) should equal(Vector(2, 3, 4))
  }
  they can "be scaled" in {
    Vector(1,2,3).scaleBy(0) should equal(Vector(0, 0, 0))
    Vector(1,2,3).scaleBy(-1) should equal(Vector(-1, -2, -3))
  }
  they can "be dot multiplied" in {
    Vector(3, 3, 3) ∙ Vector(2, 2, 2) should equal(18)
    Vector(3, 3, 3).dot(Vector(2, 2, 2)) should equal(18)
    Vector(-2.0, -5.0, 7.0) ∙ Vector(3.0, 1.0, 0.0) should equal(-11.0)
  }

}
