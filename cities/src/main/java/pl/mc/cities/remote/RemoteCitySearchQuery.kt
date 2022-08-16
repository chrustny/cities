package pl.mc.cities.remote

import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.application.query.CitySearchQueryException
import pl.mc.cities.domain.City

class RemoteCitySearchQuery(
  private val cityService: CityService
) : CitySearchQuery {
  override suspend fun find(query: String): List<City> {
    try {
      return cityService.queryCities(query).cities.mapNotNull {
        try {
          it.toCity()
        } catch (error: IllegalArgumentException) {
          null
        }
      }
    } catch (error: Throwable) {
      throw CitySearchQueryException()
    }
  }
}