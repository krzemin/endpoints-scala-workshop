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
    deletePizzaRoute
  )

  def listPizzasRoute: Route =
    listPizzas.implementedBy(_ => repository.findAllPizzas)

  def getPizzaRoute: Route =
    getPizza.implementedBy(repository.findPizzaById)

  def putPizzaRoute: Route =
    putPizza.implementedBy(repository.upsertPizza)

  def deletePizzaRoute: Route =
    deletePizza.implementedBy(repository.deletePizzaById)

}
