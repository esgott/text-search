package test

import test.Index.FoundIn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class IndexTest extends AnyFlatSpec with Matchers {

  "Index" should "be created from lines" in {
    val lines = List(
      "w1 w2 w3",
      "w4. w5, w6",
      "w7 w1 w2"
    )

    Index.fromLines(lines.iterator, "lines.txt") shouldBe Right(
      Index(
        Map(
          "w1" -> Set(FoundIn("lines.txt", 2)),
          "w2" -> Set(FoundIn("lines.txt", 2)),
          "w3" -> Set(FoundIn("lines.txt", 1)),
          "w4" -> Set(FoundIn("lines.txt", 1)),
          "w5" -> Set(FoundIn("lines.txt", 1)),
          "w6" -> Set(FoundIn("lines.txt", 1)),
          "w7" -> Set(FoundIn("lines.txt", 1))
        ),
        Set("lines.txt")
      )
    )
  }

  it should "merge" in {
    val i1 = Index(
      Map(
        "w1" -> Set(FoundIn("f1", 1)),
        "w2" -> Set(FoundIn("f1", 1))
      ),
      Set("f1")
    )

    val i2 = Index(
      Map(
        "w2" -> Set(FoundIn("f2", 1)),
        "w3" -> Set(FoundIn("f2", 1))
      ),
      Set("f2")
    )

    Index.merge(List(i1, i2)) shouldBe Index(
      Map(
        "w1" -> Set(FoundIn("f1", 1)),
        "w2" -> Set(FoundIn("f1", 1), FoundIn("f2", 1)),
        "w3" -> Set(FoundIn("f2", 1))
      ),
      Set("f1", "f2")
    )
  }

  it should "merge with itself" in {
    val i1 = Index(
      Map(
        "w1" -> Set(FoundIn("f1", 1)),
        "w2" -> Set(FoundIn("f1", 1))
      ),
      Set("f1")
    )

    Index.merge(List(i1, i1)) shouldBe Index(
      Map(
        "w1" -> Set(FoundIn("f1", 2)),
        "w2" -> Set(FoundIn("f1", 2))
      ),
      Set("f1")
    )
  }

}
