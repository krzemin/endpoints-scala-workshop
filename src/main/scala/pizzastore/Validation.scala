package pizzastore

import endpoints.algebra
import endpoints.algebra.Documentation

trait Validation extends algebra.Responses {

  def validated[A](response: Response[A], invalidDocs: Documentation): Response[Either[List[String], A]]

}
