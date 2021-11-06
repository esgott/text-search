package test

// poor man's cats
object util {

  implicit class SquenceOps[L, R](l: List[Either[L, R]]) {

    def sequence: Either[L, List[R]] =
      l.foldRight(Right(List.empty): Either[L, List[R]]) { (nextE, e) =>
        for {
          sofar <- e
          next  <- nextE
        } yield next :: sofar
      }

  }

  implicit class EitherOps[L, R](e: Either[L, R]) {

    // cats.ApplicativeError, or maybe in the future: https://contributors.scala-lang.org/t/add-either-recover/3871
    def recover(pf: PartialFunction[L, R]): Either[L, R] = e match {
      case Left(l) if pf.isDefinedAt(l) => Right(pf(l))
      case _                            => e
    }

  }

}
