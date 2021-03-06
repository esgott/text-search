package test

import test.ReadFileError._
import test.util._

import java.time.{Duration, Instant}
import scala.annotation.tailrec
import scala.io.StdIn.readLine

object SimpleSearch {

  def main(args: Array[String]): Unit =
    readFile(args).fold(printError, idx => iterate(idx, Rank.weighted(idx)))

  def readFile(args: Array[String]): Either[ReadFileError, Index] = {
    for {
      path      <- args.headOption.toRight(ReadFileError.MissingPathArg)
      start      = Instant.now
      directory <- Directory(path)
      indexes   <- directory.using { dir =>
                     dir.files.map(f =>
                       Index.fromLines(f.source.getLines, f.name).recover {
                         case ReadFileError.InvalidTextFile(fileName, t) =>
                           println(
                             s"Warning: Invalid text file $fileName (${t.getClass.getSimpleName}): ${t.getMessage}"
                           )
                           Index.empty(fileName)
                       }
                     )
                   }.sequence
      index      = Index.merge(indexes)
      end        = Instant.now
      duration   = Duration.between(start, end).toMillis
      _          = println(s"Indexing complete, took $duration ms")
    } yield index
  }

  def printError(e: ReadFileError): Unit = e match {
    case MissingPathArg                 =>
      println("No path argument given")
    case DirectoryNotFound(fileName, t) =>
      val className = t.getClass.getSimpleName
      println(s"Directory $fileName not found ($className): ${t.getMessage}")
    case InvalidTextFile(fileName, t)   =>
      val className = t.getClass.getSimpleName
      val msg       = t.getMessage
      println(s"$fileName is not a valid text file ($className): $msg")
    case ReadingError(fileName, t)      =>
      val className = t.getClass.getSimpleName
      println(s"Unable to read file $fileName ($className): ${t.getMessage}")
  }

  @tailrec
  def iterate(index: Index, rank: Rank): Unit = {
    print(s"search> ")
    Option(readLine()) match {
      case Some(":quit") | None =>
        println("\nBye")

      case Some(rankName) if rankName.startsWith("!") =>
        iterate(index, newRank(rankName.drop(1), index, rank))

      case Some(searchString) =>
        val words  = searchString.split(' ').toList
        val result = rank.search(words).take(10)
        if (result.isEmpty)
          println("no matches found")
        else
          println(result.map(r => s"${r.word} : ${r.score}%").mkString(" "))
        iterate(index, rank)
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
