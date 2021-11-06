package test

trait Rank {
  import Rank._

  def search(words: List[String]): List[Result]

}

object Rank {

  case class Result(fileName: String, score: Int)

  def forName(name: String, index: Index): Option[Rank] = name match {
    case "empty" => Some(emptyRank)
    case _       => None
  }

  def emptyRank: Rank =
    (_: List[String]) => List.empty

}
