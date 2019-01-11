# endpoints-scala-workshop
Materials for endpoints Scala library workshop

There are few exercises in this repository that aims to
get you familiar with Scala [endpoints](http://julienrf.github.io/endpoints/)
library.


### Pizza Store

You are given few data types that describe simple pizza store service,
where you can manage list of pizza menu entries, their ingredients
and prices, depending on their size.


```scala
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
```

Class `pizzastore.Repository` contains simple, in-memory implementation of pizza menu
that is used in this workshop.


### Ex 1 - API endpoints for ingredients management

Define 3 following API endpoints:

* `GET /pizzas/<id>/ingredients` - it should return ingredients of specified pizza as JSON list
* `PUT /pizzas/<id>/ingredients` - it should upsert ingredient to specified pizza
* `DELETE /pizzas/<id>/ingredients/<name>` - it should delete specified ingredient in specified pizza

Modify `TestClient` to make Hawai pizza ingredients update with these 3 endpoints.


### Ex 2 - Support 404 errors correctly

Note that trying to access pizza by non-existing `id`, the exception will be thrown.
This is because `pizzastore.Repository` has some defect. You need to fix this defect, but the fix
also affects return type of few methods.

Once you modify repository type signatures, they don't match to the signatures required
by the server implementation. On the other hand, we would like to return 404 response codes
explicitly for non-existing resources (instead of 500 signalling some case not handled properly).

With endpoints, you don't control response codes from server implementation! For such use cases you need
algebra that allows you to explicitly express value optionality. Hopefully, endpoints come with
standard response operation `wheneverFound` (or `.orNotFound` extension method).
See https://github.com/julienrf/endpoints/blob/master/algebras/algebra/src/main/scala/endpoints/algebra/Responses.scala

As a part of the task, write a test using the client that demonstrates that when trying
non-existing pizzas, client returns `None`.

What about OpenAPI documentation?


### Ex 3 - validation algebra

Ok, let's add few requirements to our API. Adding or updating pizza with empty name is not nice.
Similarly, we shouldn't allow pizzas with empty ingredients list to be present in our store. Let's add
some validation to our `PUT /pizzas` endpoint!

The endpoint should:
- upsert only pizzas with non-empty name and ingredient list, returning 200 response code
- return json containing list of problems, with 422 response code

For example:
```
PUT /pizzas
{
  "id": 6,
  "name": "",
  "ingredients": []
  "variants": []
}
```

should return
```
HTTP/422
[
  "name is empty!",
  "ingredients list is empty!"
]
```

Additional validation rules are up to you :)

Write the test using generated client, that verifies your validation. Make sure
the documentation is aligned, so that it mentions 422 code as validation error.


### Ex 4 - authentication

It's time to secure our API!

In this task you need to make sure, that endpoints modifying content of the store
(`PUT`, `POST`, `DELETE`) are secured by HTTP Basic Authentication. To perform this
task, `endpoints.algebra.BasicAuthentication` might be useful.

As usually:
- make sure that you write test that invokes secured endpoints without
  credentials and you get appropriate http status code
- make sure the docs reflect security mechanism for secured endpoints
