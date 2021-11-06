package test

trait Rank {
  import Rank._

  def search(words: List[String]): List[Result]

}

object Rank {

  case class Result(word: String, score: Int)

  def forName(name: String, index: Index): Option[Rank] = name match {
    case "empty"  => Some(empty)
    case "linear" => Some(linear(index))
    case _        => None
  }

  def empty: Rank =
    (_: List[String]) => List.empty

  def linear(index: Index): Rank =
    (words: List[String]) =>
      {
        for {
          word <- words
          hit   = index.filesFor(word)
          score = (hit.size.toFloat / index.files.size * 100).toInt
        } yield Result(word, score)
      }.sortBy(_.score).reverse

}
