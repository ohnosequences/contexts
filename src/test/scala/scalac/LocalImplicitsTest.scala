package ohnosequences.scalac.test

import org.scalatest.{ WordSpec, Matchers }

import context._

object context {

  object T1 {

    type X = String
    val x: X = "hola"
  }

  object T2 {

    type X = Int
    val x: X = 2
  }

  class U(val msg: String) {

    def print: String =
      s"listen: ${msg}"
  }

  object HasImplicitInt {

    implicit val x: Int = 2
  }
}

class LocalImports extends WordSpec with Matchers {

  "inside" should {

    "be able to use object type members and vals" in {

      val result =
        inside(T1) { x }

      val u1 = result shouldBe "hola"

      val result2 =
        inside(T2) { x }

      val u2 = result2 shouldBe 2
    }

    "shadow members of first arg if second arg has them" in {

      val result2 =
        inside(T1, T2) { x }

      val u2 = result2 shouldBe 2
    }

    "be able to use new _" in {

      val res =
        inside(new U("hola")) { print }

      res shouldBe "listen: hola"
    }

    "not support overriding an implicit declared in the same scope as the expression" in {
        """
        implicit val x0 = 1
        inside(HasImplicitInt) { implicitly[Int] }
        """ shouldNot compile
      }
  }
}
