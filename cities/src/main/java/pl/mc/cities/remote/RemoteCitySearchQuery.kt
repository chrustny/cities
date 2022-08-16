package pl.mc.cities.remote

import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.domain.City

class RemoteCitySearchQuery(

) : CitySearchQuery{
  override suspend fun find(query: String): City {

  }
}