package test

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import test.Rank.Result

class RankTest extends AnyFlatSpec with Matchers {

  val index = Index(
    Map(
      "w1" -> Set("f1", "f2"),
      "w2" -> Set("f3", "f4", "f5", "f6")
    ),
    Set("f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8")
  )

  "Empty rank" should "return empty result" in {
    Rank.empty.search(List("w1, w2")) shouldBe List.empty
  }

  "Linear rank" should "score words proportionally to how many files it has been found in" in {
    Rank.linear(index).search(List("w1")) shouldBe List(Result("w1", 25))
  }

  it should "sort results" in {
    Rank.linear(index).search(List("w1", "w2")) shouldBe List(
      Result("w2", 50),
      Result("w1", 25)
    )
  }

}
