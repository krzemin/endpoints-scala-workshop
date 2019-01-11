package pizzastore

import endpoints.{algebra, generic}
import pizzastore.model._

trait JsonSchemas extends algebra.JsonSchemas with generic.JsonSchemas  {

  implicit def pizzaSchema: JsonSchema[Pizza] =
    genericJsonSchema[Pizza]

  implicit def sizeSchema: Enum[Size] =
    enumeration[Size](Seq(Small, Medium, Big))(_.toString.toLowerCase)

  implicit def variantSchema: JsonSchema[Variant] =
    genericJsonSchema[Variant]

  implicit def priceSchema: JsonSchema[Price] =
    genericJsonSchema[Price]

}
