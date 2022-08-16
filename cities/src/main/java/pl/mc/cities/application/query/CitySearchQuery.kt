package pl.mc.cities.application.query

import pl.mc.cities.domain.City

interface CitySearchQuery {
  suspend fun find(query: String): City
}

class CitySearchQueryException(
  message: String? = null,
  cause: Throwable? = null
) : Throwable(message, cause)