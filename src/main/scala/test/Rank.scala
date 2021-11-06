package test

trait Rank {
  import Rank._

  def search(words: List[String]): List[Result]

}

object Rank {

  case class Result(word: String, score: Int)

  def forName(name: String, index: Index): Option[Rank] = name match {
    case "empty" => Some(empty)
    case _       => None
  }

  def empty: Rank =
    (_: List[String]) => List.empty

}
