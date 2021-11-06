package test

import java.time.{Duration, Instant}
import scala.annotation.tailrec
import scala.io.StdIn.readLine

object SimpleSearch {

  def main(args: Array[String]): Unit =
    readFile(args).fold(println, iterate)

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
  def iterate(indexedFiles: Index): Unit = {
    print(s"search> ")
    Option(readLine()) match {
      case Some(searchString) =>
        // TODO: Make it print the ranking of each file and its corresponding score
        iterate(indexedFiles)

      case None =>
        println("\nBye")
    }
  }

}
