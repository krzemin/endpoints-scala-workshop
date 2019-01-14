package pizzastore

import endpoints.algebra
import pizzastore.model._

trait Endpoints
  extends algebra.Endpoints
    with algebra.JsonSchemaEntities
    with JsonSchemas
    with Validation {

  def listPizzas: Endpoint[Unit, List[Pizza]] = endpoint(
    get(path / "pizzas"),
    jsonResponse[List[Pizza]](docs = Some("List of available Pizzas in menu")),
  )

  def getPizza: Endpoint[Int, Option[Pizza]] = endpoint(
    get(path / "pizzas" / segment[Int](name = "id")),
    jsonResponse[Pizza](docs = Some("Pizza found by id"))
      .orNotFound(Some("Pizza not found")),
  )

  def putPizza: Endpoint[Pizza, Either[List[String], Unit]] = endpoint(
    put(path / "pizzas", jsonRequest[Pizza](docs = Some("Pizza"))),
    validated(
      emptyResponse(docs = Some("Pizza upserted")),
      invalidDocs = Some("Can't update - invalid entity!")
    )
  )

  def deletePizza: Endpoint[Int, Unit] = endpoint(
    delete(path / "pizzas" / segment[Int](name = "id")),
    emptyResponse(docs = Some("Pizza deleted"))
  )

  def getIngredients: Endpoint[Int, Option[List[String]]] = endpoint(
    get(path / "pizzas" / segment[Int](name = "id") / "ingredients"),
    jsonResponse[List[String]](docs = Some("List of specified pizza ingredients"))
      .orNotFound(Some("Pizza not found"))
  )

  def putIngredient: Endpoint[(Int, String), Option[Unit]] = endpoint(
    put(
      path / "pizzas" / segment[Int](name = "id") / "ingredients",
      jsonRequest[String](docs = Some("Ingredient name"))
    ),
    emptyResponse(docs = Some("Pizza ingredient upserted"))
      .orNotFound(Some("Pizza not found"))
  )

  def deleteIngredient: Endpoint[(Int, String), Option[Either[List[String], Unit]]] = endpoint(
    delete(path / "pizzas" / segment[Int](name = "id") / "ingredients" / segment[String](name = "ingredient")),
    validated(
      emptyResponse(docs = Some("Ingredient removed")),
      invalidDocs = Some("Can't delete single ingredient")
    )
      .orNotFound(Some("Pizza not found"))
  )

}
