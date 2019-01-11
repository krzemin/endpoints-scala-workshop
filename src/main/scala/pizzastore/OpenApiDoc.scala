package pizzastore

import endpoints.openapi
import endpoints.openapi.model.{Info, OpenApi}
import io.circe.Json
import io.circe.syntax._

object OpenApiDoc
  extends Endpoints
    with openapi.Endpoints
    with openapi.JsonSchemaEntities {

  def openapi: OpenApi = openApi(Info("Pizza Store", "1.0.0"))(
    listPizzas,
    getPizza,
    putPizza,
    deletePizza
  )

  def openapiJson: Json =
    openapi.asJson
}
