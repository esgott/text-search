package test

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import test.Index.FoundIn
import test.Rank.Result

class RankTest extends AnyFlatSpec with Matchers {

  private val index = Index(
    Map(
      "w1" -> Set(FoundIn("f1", 1), FoundIn("f2", 2)),
      "w2" -> Set(
        FoundIn("f3", 1),
        FoundIn("f4", 2),
        FoundIn("f5", 3),
        FoundIn("f6", 4)
      ),
      "w3" -> Set(
        FoundIn("f1", 1),
        FoundIn("f2", 1),
        FoundIn("f3", 1),
        FoundIn("f4", 1),
        FoundIn("f5", 1),
        FoundIn("f6", 1),
        FoundIn("f7", 1),
        FoundIn("f8", 1)
      )
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

  it should "score 100 for word in all of the files" in {
    Rank.linear(index).search(List("w3")) shouldBe List(Result("w3", 100))
  }

  it should "score 0 for word in none of the files" in {
    Rank.linear(index).search(List("w4")) shouldBe List(Result("w4", 0))
  }

  "Weighted rank" should "score words propotionally to how many hits are in each file" in {
    Rank.weighted(index).search(List("w1")) shouldBe List(Result("w1", 33))
    // maxHit is 3+6=9, w1 gets 3 hits, 3/9=0.333
  }

  it should "sort results" in {
    Rank.weighted(index).search(List("w1", "w2")) shouldBe List(
      Result("w2", 71),
      Result("w1", 21)
    )
    // maxHit is 10+4=14, w1: 3/14=0.214, w2: 10/14=0.714
  }

  it should "score 100 for word in all of the files" in {
    Rank.weighted(index).search(List("w3")) shouldBe List(Result("w3", 100))
  }

  it should "score 0 for word in none of the files" in {
    Rank.weighted(index).search(List("w4")) shouldBe List(Result("w4", 0))
  }

}
