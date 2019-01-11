package pizzastore

import endpoints.algebra
import pizzastore.model._

trait Endpoints
  extends algebra.Endpoints
    with algebra.JsonSchemaEntities
    with JsonSchemas {

  def listPizzas: Endpoint[Unit, List[Pizza]] = endpoint(
    get(path / "pizzas"),
    jsonResponse[List[Pizza]](docs = Some("List of available Pizzas in menu")),
  )

  def getPizza: Endpoint[Int, Pizza] = endpoint(
    get(path / "pizzas" / segment[Int](name = "id")),
    jsonResponse[Pizza](docs = Some("Pizza found by id")),
  )

  def putPizza: Endpoint[Pizza, Unit] = endpoint(
    put(path / "pizzas", jsonRequest[Pizza](docs = Some("Pizza"))),
    emptyResponse(docs = Some("Pizza upserted"))
  )

  def deletePizza: Endpoint[Int, Unit] = endpoint(
    delete(path / "pizzas" / segment[Int](name = "id")),
    emptyResponse(docs = Some("Pizza deleted"))
  )

}
