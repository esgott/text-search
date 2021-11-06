package test

import java.time.{Duration, Instant}
import scala.annotation.tailrec
import scala.io.StdIn.readLine

object SimpleSearch {

  def main(args: Array[String]): Unit =
    readFile(args).fold(println, iterate(_, Rank.emptyRank))

  def readFile(args: Array[String]): Either[ReadFileError, Index] = {
    for {
      path      <- args.headOption.toRight(ReadFileError.MissingPathArg)
      start      = Instant.now
      directory <- Directory(path)
      indexes    = directory.using { dir =>
                     dir.files.map(f => Index.fromLines(f.source.getLines, f.name))
                   }
      index      = Index.merge(indexes)
      end        = Instant.now
      duration   = Duration.between(start, end).toMillis
      _          = println(s"Indexing complete, took $duration ms")
    } yield index
  }

  @tailrec
  def iterate(index: Index, rank: Rank): Unit = {
    print(s"search> ")
    Option(readLine()) match {
      case Some(rankName) if rankName.startsWith("!") =>
        iterate(index, newRank(rankName.drop(1), index, rank))

      case Some(searchString) =>
        val words  = searchString.split(' ').toList
        val result = rank.search(words)
        if (result.isEmpty)
          println("no matches found")
        else
          println(result.map(r => s"${r.fileName} : ${r.score}%").mkString(" "))
        iterate(index, rank)

      case None =>
        println("\nBye")
    }
  }

  private def newRank(name: String, index: Index, oldRank: Rank): Rank =
    Rank.forName(name, index) match {
      case Some(rank) =>
        println(s"Rank updated to '$name'")
        rank

      case None =>
        println(s"Unknown rank: '$name'")
        oldRank
    }

}
