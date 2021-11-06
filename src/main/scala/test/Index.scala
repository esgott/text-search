package test

import java.nio.charset.MalformedInputException
import scala.util.Try

case class Index(words: Map[String, Set[String]]) {
  import Index._

  def filesFor(word: String): Set[String] =
    words.getOrElse(tokenize(word), Set.empty)

}

object Index {

  def fromLines(lines: Iterator[String], fileName: String): Index = {
    val empty = Map.empty[String, Set[String]]

    val index = Try {
      lines
        .flatMap(_.split(' '))
        .map(tokenize)
        .foldLeft(empty) { (map, word) =>
          map.updated(word, Set(fileName))
        }
    }.recover { case _: MalformedInputException =>
      println(s"Warning: binary file found: $fileName")
      empty
    }.get

    Index(index)
  }

  private def tokenize(word: String): String =
    word.replaceAll("[^A-Za-z0-9]", "")

  def merge(is: List[Index]): Index =
    Index(
      is
        .map(_.words.keySet)
        .reduce(_ ++ _)
        .map(word => word -> is.flatMap(_.words.get(word)).toSet.flatten)
        .toMap
    )

}
