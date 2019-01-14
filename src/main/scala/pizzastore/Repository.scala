package pizzastore

import pizzastore.model._

class Repository(initialMenu: List[Pizza]) {

  private var menu: Map[Int, Pizza] =
    initialMenu.map(p => p.id -> p).toMap

  def findAllPizzas: List[Pizza] = {
    menu.values.toList.sortBy(_.id)
  }

  def findPizzaById(id: Int): Option[Pizza] = {
    menu.get(id)
  }

  def upsertPizza(pizza: Pizza): Unit = {
    menu += pizza.id -> pizza
  }

  def deletePizzaById(id: Int): Unit = {
    menu -= id
  }
}
