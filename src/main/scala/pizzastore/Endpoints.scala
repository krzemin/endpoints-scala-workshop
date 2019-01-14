package pizzastore

import endpoints.algebra
import endpoints.algebra.BasicAuthentication
import pizzastore.model._

trait Endpoints
  extends algebra.Endpoints
    with algebra.JsonSchemaEntities
    with JsonSchemas
    with Validation
    with BasicAuthentication {

  def listPizzas: Endpoint[Unit, List[Pizza]] = endpoint(
    get(path / "pizzas"),
    jsonResponse[List[Pizza]](docs = Some("List of available Pizzas in menu")),
  )

  def getPizza: Endpoint[Int, Option[Pizza]] = endpoint(
    get(path / "pizzas" / segment[Int](name = "id")),
    jsonResponse[Pizza](docs = Some("Pizza found by id"))
      .orNotFound(Some("Pizza not found")),
  )

  def putPizza: Endpoint[(Pizza, BasicAuthentication.Credentials), Option[Either[List[String], Unit]]] =
    authenticatedEndpoint(
      Put,
      path / "pizzas",
      requestEntity = jsonRequest[Pizza](docs = Some("Pizza")),
      response = validated(
        emptyResponse(docs = Some("Pizza upserted")),
        invalidDocs = Some("Can't update - invalid entity!")
      )
  )

  def deletePizza: Endpoint[(Int, BasicAuthentication.Credentials), Option[Unit]] =
    authenticatedEndpoint(
      Delete,
      path / "pizzas" / segment[Int](name = "id"),
      requestEntity = emptyRequest,
      response = emptyResponse(docs = Some("Pizza deleted"))
    )

  def getIngredients: Endpoint[Int, Option[List[String]]] = endpoint(
    get(path / "pizzas" / segment[Int](name = "id") / "ingredients"),
    jsonResponse[List[String]](docs = Some("List of specified pizza ingredients"))
      .orNotFound(Some("Pizza not found"))
  )

  def putIngredient: Endpoint[(Int, String, BasicAuthentication.Credentials), Option[Option[Unit]]] =
    authenticatedEndpoint(
      Put,
      path / "pizzas" / segment[Int](name = "id") / "ingredients",
      requestEntity = jsonRequest[String](docs = Some("Ingredient name")),
      response = emptyResponse(docs = Some("Pizza ingredient upserted"))
        .orNotFound(Some("Pizza not found"))
  )

  def deleteIngredient: Endpoint[(Int, String, BasicAuthentication.Credentials), Option[Option[Either[List[String], Unit]]]] =
    authenticatedEndpoint(
      Delete,
      path / "pizzas" / segment[Int](name = "id") / "ingredients" / segment[String](name = "ingredient"),
      requestEntity = emptyRequest,
      response =
        validated(
          emptyResponse(docs = Some("Ingredient removed")),
          invalidDocs = Some("Can't delete single ingredient")
        )
        .orNotFound(Some("Pizza not found"))
    )

}
