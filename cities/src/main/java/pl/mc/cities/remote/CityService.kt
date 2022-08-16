package pl.mc.cities.remote

import com.fasterxml.jackson.annotation.JsonProperty
import pl.mc.cities.domain.City
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {

  @GET("https://secure.geonames.org/searchJSON?maxRows=10&username=keep_truckin")
  fun queryCities(@Query("name_startsWith") query: String)
}

data class CitiesResponseRemoteModel(
  @JsonProperty("geonames")
  val cities: List<CityRemoteModel>
)

data class CityRemoteModel(
  @JsonProperty("toponymName")
  val cityName: String?,
  @JsonProperty("countryName")
  val countryName: String?,
  @JsonProperty("adminName1")
  val stateName: String?
) {
  fun toCity(): City {
    return City.create(
      cityName = requireNotNull(cityName),
      countryName = requireNotNull(countryName),
      stateName = requireNotNull(stateName)
    )
  }
}
