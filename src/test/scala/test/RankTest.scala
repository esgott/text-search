package test

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RankTest extends AnyFlatSpec with Matchers {

  "Empty rank" should "return empty result" in {
    Rank.empty.search(List("w1, w2")) shouldBe List.empty
  }

}
