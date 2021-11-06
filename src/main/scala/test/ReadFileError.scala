package test

sealed trait ReadFileError

object ReadFileError {

  case object MissingPathArg                 extends ReadFileError
  case class DirectoryNotFound(t: Throwable) extends ReadFileError

}
