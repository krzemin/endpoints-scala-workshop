package pizzastore.model

case class Pizza(id: Int,
                 name: String,
                 ingredients: List[String],
                 variants: List[Variant])


case class Variant(size: Size,
                   price: Price)

sealed trait Size
case object Small extends Size
case object Medium extends Size
case object Big extends Size

case class Price(amount: BigDecimal,
                 currency: String)
