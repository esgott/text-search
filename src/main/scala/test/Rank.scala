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

  /** A very simple ranking algorithm, that never finds a match.
    */
  def empty: Rank =
    (_: List[String]) => List.empty

  /** The score for a result is proportional to the number of files the word is
    * found in, e.g. 100% if it can be found in all the files, 0% if in none of
    * them, 50% if in half of them.
    */
  def linear(index: Index): Rank =
    (words: List[String]) =>
      sorted {
        for {
          word <- words
          hit   = index.filesFor(word)
          score = (hit.size.toFloat / index.files.size * 100).toInt
        } yield Result(word, score)
      }

  /** The score is weighted towards the results where the word can be found more
    * times in the files. For each search a maximum hit is calculated, which is
    * the maximum occurrence of the words plus the files where there wasn't any
    * occurrence (this ensures that it can only get 100% if there was occurrence
    * in all files). Then the score is calculated by the ratio between the
    * number of occurrences and the max hit.
    */
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
