package pizzastore

import com.softwaremill.sttp.{HttpURLConnectionBackend, Id}
import endpoints.algebra.BasicAuthentication.Credentials
import pizzastore.model._

object TestClient extends App {

  val sttpBackend = HttpURLConnectionBackend()
  val client = new SttpClient[Id]("http://localhost:5000", sttpBackend)

  val validCredentials = Credentials("pizzaman", "secret")

  println("Available pizzas:")
  client.listPizzas(()).foreach(println)
  println("---")


  val salamiPizza = Pizza(
    id = 10,
    name = "Salami",
    ingredients = List("cheese", "salami", "paprika"),
    variants = List(
      Variant(Small, Price(22, "PLN")),
      Variant(Medium, Price(25.50, "PLN")),
      Variant(Big, Price(235, "PLN"))
    )
  )

  println("Trying invalid pizza upserts...")
  println(client.putPizza(salamiPizza.copy(name = ""), validCredentials))
  println(client.putPizza(salamiPizza.copy(ingredients = Nil), validCredentials))
  println(client.putPizza(salamiPizza.copy(name = "", ingredients = Nil), validCredentials))

  println("Trying to remove the only ingredient from Margherita")
  println(client.deleteIngredient(1, "cheese", validCredentials))

  println("Adding Salami...")
//  client.putPizza(salamiPizza)

//  val hawaii = client.getPizza(3)
//  val upgradedHawaii = hawaii.copy(ingredients =
//    hawaii.ingredients.map { ingredient =>
//      if(ingredient == "cheese") "onion" else ingredient
//    }
//  )

  println("Upgrading Hawaii")
//  client.putPizza(upgradedHawaii)

  client.deleteIngredient(3, "cheese", validCredentials)
  client.putIngredient(3, "onion", validCredentials)


  println("Deleting Capricciosa")
  client.deletePizza(2, validCredentials)

  println("---")
  println("Available pizzas (after changes):")
  client.listPizzas(()).foreach(println)

  print("Trying out non-existing pizza...")
  println("returns " + client.getPizza(999))

  print("Trying out without valid credentials...")
  println("returns " + client.deletePizza(1, Credentials("foo", "bar")))

}
