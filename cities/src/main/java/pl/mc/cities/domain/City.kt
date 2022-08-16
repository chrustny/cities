package pl.mc.cities.domain

data class City(
  val name: String,
  val state: State
) {
  companion object {
    fun create(cityName: String, stateName: String, countryName: String): City {
      return City(
        name = cityName,
        state = State(
          name = stateName,
          country = Country(countryName)
        )
      )
    }
  }
}

data class Country(
  val name: String
)

data class State(
  val name: String,
  val country: Country
)