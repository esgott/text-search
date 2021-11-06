package test

import test.Index._

import java.nio.charset.MalformedInputException
import scala.util.Try

case class Index(words: Map[String, Set[FoundIn]], files: Set[String]) {

  def filesFor(word: String): Set[FoundIn] =
    words.getOrElse(tokenize(word), Set.empty)

}

object Index {

  case class FoundIn(fileName: String, times: Int) {
    def found: FoundIn = copy(times = times + 1)
  }

  def fromLines(lines: Iterator[String], fileName: String): Index = {
    val empty = Map.empty[String, Set[FoundIn]]

    val index = Try {
      lines
        .flatMap(_.split(' '))
        .map(tokenize)
        .foldLeft(empty) { (map, word) =>
          map.updatedWith(word) {
            case None          => Some(Set(FoundIn(fileName, 1)))
            case Some(foundIn) => Some(foundIn.map(_.found))
          }
        }
    }.recover { case _: MalformedInputException =>
      println(s"Warning: binary file found: $fileName")
      empty
    }.get

    Index(index, Set(fileName))
  }

  private def tokenize(word: String): String =
    word.replaceAll("[^A-Za-z0-9]", "")

  def merge(is: List[Index]): Index =
    Index(
      words = is
        .map(_.words.keySet)
        .reduce(_ ++ _)
        .map(word =>
          word -> mergeFoundIns(is.flatMap(_.words.get(word)).flatten)
        )
        .toMap,
      files = is.map(_.files).toSet.flatten
    )

  private def mergeFoundIns(fs: List[FoundIn]): Set[FoundIn] =
    fs.groupBy(_.fileName)
      .map { case (fileName, foundIns) =>
        FoundIn(fileName, foundIns.map(_.times).sum)
      }
      .toSet

}
