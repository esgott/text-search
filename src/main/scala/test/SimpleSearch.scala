package test

import java.io.File
import scala.util.Try

object SimpleSearch {

  def main(args: Array[String]): Unit =
    Program
      .readFile(args)
      .fold(
        println,
        file => Program.iterate(Program.index(file))
      )

}

object Program {
  import scala.io.StdIn.readLine

  case class Index() // TODO: Implement this

  sealed trait ReadFileError

  case object MissingPathArg             extends ReadFileError
  case class NotDirectory(error: String) extends ReadFileError
  case class FileNotFound(t: Throwable)  extends ReadFileError

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }

  // TODO: Index all files in the directory
  def index(file: File): Index = ???

  def iterate(indexedFiles: Index): Unit = {
    print(s"search> ")
    val searchString = readLine()
    // TODO: Make it print the ranking of each file and its corresponding score
    iterate(indexedFiles)
  }
}
