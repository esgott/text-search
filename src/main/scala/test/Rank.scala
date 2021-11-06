package test

trait Rank {
  import Rank._

  def search(words: List[String]): List[Result]

}

object Rank {

  case class Result(word: String, score: Int)

  def forName(name: String, index: Index): Option[Rank] = name match {
    case "empty"    => Some(empty)
    case "linear"   => Some(linear(index))
    case "weighted" => Some(weighted(index))
    case _          => None
  }

  def empty: Rank =
    (_: List[String]) => List.empty

  def linear(index: Index): Rank =
    (words: List[String]) =>
      sorted {
        for {
          word <- words
          hit   = index.filesFor(word)
          score = (hit.size.toFloat / index.files.size * 100).toInt
        } yield Result(word, score)
      }

  def weighted(index: Index): Rank =
    (words: List[String]) =>
      sorted {
        val maxHit = words
          .map(index.filesFor)
          .map { hits =>
            val missingFiles = index.files -- hits.map(_.fileName)
            hits.map(_.times).sum + missingFiles.size
          }
          .max

        for {
          word <- words
          hits  = index.filesFor(word).map(_.times).sum
          score = (hits.toFloat / maxHit * 100).toInt
          _     = println(s"$word: hits=$hits maxHit=$maxHit")
        } yield Result(word, score)
      }

  private def sorted(l: List[Result]): List[Result] =
    l.sortBy(_.score).reverse

}
