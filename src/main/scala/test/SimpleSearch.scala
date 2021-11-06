package test

object SimpleSearch {

  def main(args: Array[String]): Unit =
    Program
      .readFile(args)
      .fold(
        println,
        Program.iterate
      )

}

object Program {
  import scala.io.StdIn.readLine

  def readFile(args: Array[String]): Either[ReadFileError, Directory] = {
    for {
      path      <- args.headOption.toRight(ReadFileError.MissingPathArg)
      directory <- Directory(path)
    } yield directory
  }

  def iterate(indexedFiles: Directory): Unit = {
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
