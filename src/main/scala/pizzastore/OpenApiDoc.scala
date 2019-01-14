package pizzastore

import endpoints.algebra.Documentation
import endpoints.openapi
import endpoints.openapi.model.{Info, MediaType, OpenApi, Schema}
import io.circe.Json
import io.circe.syntax._

object OpenApiEncoder
  extends endpoints.openapi.model.OpenApiSchemas
    with endpoints.circe.JsonSchemas

import OpenApiEncoder.JsonSchema._

object OpenApiDoc
  extends Endpoints
    with openapi.Endpoints
    with openapi.JsonSchemaEntities
    with openapi.BasicAuthentication {

  def openapi: OpenApi = openApi(Info("Pizza Store", "1.0.0"))(
    listPizzas,
    getPizza,
    putPizza,
    deletePizza,
    getIngredients,
    putIngredient,
    deleteIngredient
  )

  def openapiJson: Json =
    openapi.asJson

  // openapi Validation interpreter -> ideally should live in a separate trait
  def validated[A](response: List[OpenApiDoc.DocumentedResponse], invalidDocs: Documentation): List[OpenApiDoc.DocumentedResponse] =
    response :+ OpenApiDoc.DocumentedResponse(
      status = 422,
      documentation = invalidDocs.getOrElse(""),
      content = Map(
        "application/json" -> MediaType(schema = Some(
          Schema.Array(
            Schema.simpleString,
            description = Some("validation error messages")
          )
        ))
      )
    )
}
