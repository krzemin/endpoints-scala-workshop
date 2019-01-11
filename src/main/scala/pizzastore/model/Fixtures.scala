package pizzastore.model

object Fixtures {

  val pizzasList = List(
    Pizza(
      id = 1,
      name = "Margherita",
      ingredients = List("cheese"),
      variants = List(
        Variant(Small, Price(17, "PLN")),
        Variant(Medium, Price(20, "PLN")),
        Variant(Big, Price(29, "PLN")),
      )
    ),
    Pizza(
      id = 2,
      name = "Capricciosa",
      ingredients = List("cheese", "ham", "mushrooms"),
      variants = List(
        Variant(Small, Price(21, "PLN")),
        Variant(Medium, Price(24, "PLN")),
        Variant(Big, Price(33, "PLN")),
      )
    ),
    Pizza(
      id = 3,
      name = "Hawaii",
      ingredients = List("cheese", "ham", "pineapple"),
      variants = List(
        Variant(Small, Price(22.5, "PLN")),
        Variant(Medium, Price(26, "PLN")),
        Variant(Big, Price(35.5, "PLN")),
      )
    ),
    Pizza(
      id = 4,
      name = "Quattro formaggi",
      ingredients = List("mozzarella", "stracchino", "fontina", "gorgonzola"),
      variants = List(
        Variant(Small, Price(27, "PLN")),
        Variant(Medium, Price(30.5, "PLN")),
        Variant(Big, Price(39, "PLN")),
      )
    )
  )

}
