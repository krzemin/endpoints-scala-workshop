package pizzastore

import com.softwaremill.sttp.{HttpURLConnectionBackend, Id}
import pizzastore.model._

object TestClient extends App {

  val sttpBackend = HttpURLConnectionBackend()
  val client = new SttpClient[Id]("http://localhost:5000", sttpBackend)

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
  println(client.putPizza(salamiPizza.copy(name = "")))
  println(client.putPizza(salamiPizza.copy(ingredients = Nil)))
  println(client.putPizza(salamiPizza.copy(name = "", ingredients = Nil)))

  println("Trying to remove the only ingredient from Margherita")
  println(client.deleteIngredient((1, "cheese")))

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

  client.deleteIngredient((3, "cheese"))
  client.putIngredient((3, "onion"))


  println("Deleting Capricciosa")
  client.deletePizza(2)

  println("---")
  println("Available pizzas (after changes):")
  client.listPizzas(()).foreach(println)

  print("Trying out non-existing pizza...")
  println("returns " + client.getPizza(999))

}
