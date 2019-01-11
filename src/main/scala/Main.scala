import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.{IncomingConnection, ServerBinding}
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route.asyncHandler
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import pizzastore._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends App with Directives with ErrorAccumulatingCirceSupport {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val repository = new Repository(model.Fixtures.pizzasList)
  val akkaServerRoutes = new AkkaServerRoutes(repository)

  val mainRoutes = concat(
    pathEndOrSingleSlash {
      getFromResource("web/index.html")
    },
    pathPrefix("swagger-ui") {
      getFromResourceDirectory("web/swagger-ui/")
    },
    path("openapi.json") {
      complete(OpenApiDoc.openapiJson)
    },
    akkaServerRoutes.route
  )

  val server: Source[IncomingConnection, Future[ServerBinding]] =
    Http(actorSystem).bind("localhost", 5000)

  val handler: Future[ServerBinding] =
    server.to(Sink.foreach { connection =>
      connection.handleWithAsyncHandler(asyncHandler(mainRoutes))
    }).run()

  println("Pizza store started on http://localhost:5000")

  sys.addShutdownHook {
    println("Shutting down pizza store...")
    Await.result(actorSystem.terminate(), 10.seconds)
    ()
  }

  Await.result(actorSystem.whenTerminated.map { _ =>
    println("Pizza store terminated.")
  }(actorSystem.dispatcher), Duration.Inf)
}
