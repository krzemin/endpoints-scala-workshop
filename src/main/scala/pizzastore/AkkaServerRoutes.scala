package pizzastore

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import endpoints.akkahttp
import endpoints.algebra.Documentation
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import endpoints.algebra.BasicAuthentication.Credentials

class AkkaServerRoutes(repository: Repository)
  extends pizzastore.Endpoints
  with akkahttp.server.Endpoints
  with akkahttp.server.circe.JsonSchemaEntities
  with akkahttp.server.BasicAuthentication {

  val route: Route = concat(
    listPizzasRoute,
    getPizzaRoute,
    putPizzaRoute,
    deletePizzaRoute,
    getIngredientsRoute,
    putIngredientRoute,
    deleteIngredientRoute
  )

  def listPizzasRoute: Route =
    listPizzas.implementedBy(_ => repository.findAllPizzas)

  def getPizzaRoute: Route =
    getPizza.implementedBy(repository.findPizzaById)

  def putPizzaRoute: Route =
    putPizza.implementedBy { case (pizza, credentials) =>
      authenticated(credentials) {
        val nameValidation = if(pizza.name.trim.isEmpty) List("name is empty!") else Nil
        val ingredientsValidation = if(pizza.ingredients.isEmpty) List("ingredients list is empty!") else Nil

        nameValidation ++ ingredientsValidation match {
          case Nil =>
            Right(repository.upsertPizza(pizza))
          case errors =>
            Left(errors)
        }
      }
    }

  def deletePizzaRoute: Route =
    deletePizza.implementedBy { case (id, credentials) =>
      authenticated(credentials) {
        repository.deletePizzaById(id)
      }
    }

  def getIngredientsRoute: Route =
    getIngredients.implementedBy { id =>
      repository.findPizzaById(id).map(_.ingredients)
    }

  def putIngredientRoute: Route =
    putIngredient.implementedBy { case (id, ingredient, credentials) =>
      authenticated(credentials) {
        repository.findPizzaById(id).map { pizza =>
          if (!pizza.ingredients.contains(ingredient)) {
            val newIngredients = pizza.ingredients :+ ingredient
            val newPizza = pizza.copy(ingredients = newIngredients)
            repository.upsertPizza(newPizza)
          }
        }
      }
    }

  def deleteIngredientRoute: Route =
    deleteIngredient.implementedBy { case (id, ingredient, credentials) =>
      authenticated(credentials) {
        repository.findPizzaById(id).map { pizza =>
          val newIngredients = pizza.ingredients.filterNot(_ == ingredient)
          if(newIngredients.isEmpty) {
            Left(List("Can't delete single pizza ingredient!"))
          } else {
            val newPizza = pizza.copy(ingredients = newIngredients)
            Right(repository.upsertPizza(newPizza))
          }
        }
      }
    }

  def validated[A](response: A => Route, invalidDocs: Documentation): Either[List[String], A] => Route = {
    case Left(errors) =>
      complete(StatusCodes.UnprocessableEntity -> errors)

    case Right(success) =>
      response(success)
  }

  def authenticated[A](credentials: Credentials)(whenValid: => A): Option[A] = {
    if(credentials.username == "pizzaman" && credentials.password == "secret") {
      Some(whenValid)
    } else {
      None
    }
  }
}
