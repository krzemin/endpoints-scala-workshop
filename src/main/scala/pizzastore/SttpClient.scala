package pizzastore

import com.softwaremill.sttp
import endpoints.sttp.client
import utils.sttp.JsonSchemaEntities

import scala.language.higherKinds


class SttpClient[R[_]](httpPrefix: String,
                       sttpBackend: sttp.SttpBackend[R, _])
  extends client.Endpoints(httpPrefix, sttpBackend)
    with Endpoints
    with JsonSchemaEntities[R]
