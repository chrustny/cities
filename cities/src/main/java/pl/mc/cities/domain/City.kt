package pl.mc.cities.domain

data class City(
  val name: String,
  val state: State
)

data class Country(
  val name: String
)

data class State(
  val name: String,
  val country: Country
)