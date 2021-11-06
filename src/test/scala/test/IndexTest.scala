package test

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class IndexTest extends AnyFlatSpec with Matchers {

  "Index" should "be created from lines" in {
    val lines = List(
      "w1 w2 w3",
      "w4. w5, w6",
      "w7 w1 w2"
    )

    Index.fromLines(lines.iterator, "lines.txt") shouldBe Index(
      Map(
        "w1" -> Set("lines.txt"),
        "w2" -> Set("lines.txt"),
        "w3" -> Set("lines.txt"),
        "w4" -> Set("lines.txt"),
        "w5" -> Set("lines.txt"),
        "w6" -> Set("lines.txt"),
        "w7" -> Set("lines.txt")
      )
    )
  }

  it should "merge" in {
    val i1 = Index(
      Map(
        "w1" -> Set("f1"),
        "w2" -> Set("f1")
      )
    )

    val i2 = Index(
      Map(
        "w2" -> Set("f2"),
        "w3" -> Set("f2")
      )
    )

    Index.merge(List(i1, i2)) shouldBe Index(
      Map(
        "w1" -> Set("f1"),
        "w2" -> Set("f1", "f2"),
        "w3" -> Set("f2")
      )
    )
  }

}
