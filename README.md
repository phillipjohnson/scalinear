# Scalinear
Scalinear is a basic vector and matrix library for Scala.

I built this library in response to a gap I encountered when working with OpenGL--there seems to be no simple matrix implementation readily used with Scala. The options seems to be roll your own or pull in a large library to do the work.

If you need a robust solution, please see [Breeze](https://github.com/scalanlp/breeze). By design, scalinear barely scratches the surface of what Breeze can do.

## Build
[Travis CI](http://travis-ci.org/) is used to build and test all commits.

Status: [![Build Status](https://travis-ci.org/phillipjohnson/scalinear.svg?branch=master)](https://travis-ci.org/phillipjohnson/scalinear)

## Use

### Vectors

```scala
scala> val v = Vector(2,4,6,8)
v: com.letstalkdata.scalinear.Vector[Int] = [2, 4, 6, 8]

scala> v / 2
res1: com.letstalkdata.scalinear.Vector[Int] = [1, 2, 3, 4]

scala> val u = Vector(3,3,3,3)
u: com.letstalkdata.scalinear.Vector[Int] = [3, 3, 3, 3]

scala> v + u
res2: com.letstalkdata.scalinear.Vector[Int] = [5, 7, 9, 11]
```

Vectors are created using one of the factory methods in the Vector object. In most cases the type will be inferred, but hints are sometimes required.

```scala
scala> val v = Vector(1.0, 1.0, 1.0)
v: com.letstalkdata.scalinear.Vector[Double] = [1.0, 1.0, 1.0]

scala> val v = Vector.ones(3)
<console>:13: error: ambiguous implicit values:
 both object BigIntIsIntegral in object Numeric of type scala.math.Numeric.BigIntIsIntegral.type
 and object IntIsIntegral in object Numeric of type scala.math.Numeric.IntIsIntegral.type
 match expected type Numeric[T]
       val v = Vector.ones(3)
                          ^

scala> val v = Vector.ones[Double](3)
v: com.letstalkdata.scalinear.Vector[Double] = [1.0, 1.0, 1.0]
```

Vector arithmetic is intuitive and works as you would expect. Element-wise operations simply accept a single number as their argument, compared to the vector operations which accept another Vector.

```scala
scala> val ones = Vector.ones[Int](4)
ones: com.letstalkdata.scalinear.Vector[Int] = [1, 1, 1, 1]

scala> ones + 7
res0: com.letstalkdata.scalinear.Vector[Int] = [8, 8, 8, 8]

scala> ones + Vector(7,8,9,10)
res1: com.letstalkdata.scalinear.Vector[Int] = [8, 9, 10, 11]
```

Higher order functions can be accessed via `asArray`.

```scala
scala> Vector.ones[Int](4).asArray.map(n => n * 3)
res1: Array[Int] = Array(3, 3, 3, 3)
```

### Matrices

```scala
scala> val A = Matrix(Vector(1,2), Vector(3,4), Vector(5,6))
A: com.letstalkdata.scalinear.Matrix[Int] =
[1, 2]
[3, 4]
[5, 6]

scala> val B = Matrix(Vector(1,2,3), Vector(4,5,6))
B: com.letstalkdata.scalinear.Matrix[Int] =
[1, 2, 3]
[4, 5, 6]

scala> A * B
res0: com.letstalkdata.scalinear.Matrix[Int] =
[9, 12, 15]
[19, 26, 33]
[29, 40, 51]
```

Matrices are created in a syntactically similar manner to Vectors. The difference is that Matrices are created from Vectors instead of from raw numbers.

```scala
scala> val A = Matrix(Vector(1,1,1), Vector(2,2,2))
A: com.letstalkdata.scalinear.Matrix[Int] =
[1, 1, 1]
[2, 2, 2]

scala> val B = Matrix.ones[Int](3)
B: com.letstalkdata.scalinear.Matrix[Int] =
[1, 1, 1]
[1, 1, 1]
[1, 1, 1]
```

Following the same pattern, Matrix arithmetic uses overloaded operators.

```scala
scala> val A = Matrix.eye[Int](2)
A: com.letstalkdata.scalinear.Matrix[Int] =
[1, 0]
[0, 1]

scala> A + 2
res0: com.letstalkdata.scalinear.Matrix[Int] =
[3, 2]
[2, 3]

scala> val B = Matrix(Vector(1,2), Vector(3,4))
B: com.letstalkdata.scalinear.Matrix[Int] =
[1, 2]
[3, 4]

scala> A + B
res1: com.letstalkdata.scalinear.Matrix[Int] =
[2, 2]
[3, 5]
```

Higher order functions can be accessed via `asArray`.

```scala
scala> val A = Matrix.eye[Int](2)
A: com.letstalkdata.scalinear.Matrix[Int] =
[1, 0]
[0, 1]

scala> A.asArray.forall(row => row(0) == 1)
res0: Boolean = false
```