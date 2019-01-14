package pizzastore

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import endpoints.akkahttp

class AkkaServerRoutes(repository: Repository)
  extends pizzastore.Endpoints
  with akkahttp.server.Endpoints
  with akkahttp.server.circe.JsonSchemaEntities {

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
    putPizza.implementedBy(repository.upsertPizza)

  def deletePizzaRoute: Route =
    deletePizza.implementedBy(repository.deletePizzaById)

  def getIngredientsRoute: Route =
    getIngredients.implementedBy { id =>
      repository.findPizzaById(id).ingredients
    }

  def putIngredientRoute: Route =
    putIngredient.implementedBy { case (id, ingredient) =>
      val pizza = repository.findPizzaById(id)

      if(!pizza.ingredients.contains(ingredient)) {
        val newIngredients = pizza.ingredients :+ ingredient
        val newPizza = pizza.copy(ingredients = newIngredients)
        repository.upsertPizza(newPizza)
      }
    }

  def deleteIngredientRoute: Route =
    deleteIngredient.implementedBy { case (id, ingredient) =>
      val pizza = repository.findPizzaById(id)
      val newIngredients = pizza.ingredients.filter(_ == ingredient)
      val newPizza = pizza.copy(ingredients = newIngredients)
      repository.upsertPizza(newPizza)
    }

}
