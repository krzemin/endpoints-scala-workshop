package utils.sttp

import com.softwaremill.sttp
import com.softwaremill.sttp.ResponseAs
import endpoints.algebra.Documentation
import endpoints.sttp.client.Endpoints
import endpoints.{algebra, circe}
import io.circe.parser

import scala.language.higherKinds

trait JsonSchemaEntities[R[_]] extends algebra.JsonSchemas with circe.JsonSchemas {
  this: Endpoints[R] =>

  def jsonRequest[A : JsonSchema](docs: Documentation = None): RequestEntity[A] = { (a, request) =>
    request
      .body(JsonSchema.toCirceEncoder[A].apply(a).noSpaces)
      .contentType("application/json")
  }

  def jsonResponse[A : JsonSchema](docs: Documentation = None): Response[A] = new SttpResponse[A] {
    override type ReceivedBody = Either[Exception, A]
    override def responseAs: ResponseAs[Either[Exception, A], Nothing] = sttp.asString.map { str =>
      parser.parse(str).flatMap(JsonSchema.toCirceDecoder[A].decodeJson)
    }
    override def validateResponse(response: sttp.Response[ReceivedBody]): R[A] = {
      response.unsafeBody match {
        case Right(a) => backend.responseMonad.unit(a)
        case Left(exception) => backend.responseMonad.error(exception)
      }
    }
  }
}
