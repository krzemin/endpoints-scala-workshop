package pizzastore

import com.softwaremill.sttp
import com.softwaremill.sttp.ResponseAs
import endpoints.algebra.Documentation
import endpoints.sttp.client
import io.circe.Decoder
import utils.sttp.JsonSchemaEntities
import io.circe.parser

import scala.language.higherKinds


class SttpClient[R[_]](httpPrefix: String,
                       sttpBackend: sttp.SttpBackend[R, _])
  extends client.Endpoints(httpPrefix, sttpBackend)
    with client.BasicAuthentication[R]
    with Endpoints
    with JsonSchemaEntities[R] {

  // sttp Validation interpreter -> ideally should live in a separate trait
  def validated[A](inner: SttpResponse[A], invalidDocs: Documentation): SttpResponse[Either[List[String], A]] =
    new SttpResponse[Either[List[String], A]] {
      type ReceivedBody = inner.ReceivedBody

      def responseAs: ResponseAs[ReceivedBody, Nothing] = inner.responseAs

      def validateResponse(response: sttp.Response[ReceivedBody]): R[Either[List[String], A]] = {
        response.body match {
          case Left(bodyStr) if response.code == 422 =>
            parser.parse(bodyStr).flatMap(stringListDecoder.decodeJson) match {
              case Left(decodeErr) =>
                backend.responseMonad.error(decodeErr)
              case Right(validationErrList) =>
                backend.responseMonad.unit(Left(validationErrList))
            }
          case Left(_) =>
            backend.responseMonad.error(new IllegalArgumentException(s"Unknown response code: ${response.code}"))
          case Right(_) =>
            backend.responseMonad.map(inner.validateResponse(response))(Right(_))
        }
      }
    }

  private val stringListDecoder: Decoder[List[String]] =
    Decoder.decodeList(Decoder.decodeString)

}
