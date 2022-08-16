package pl.mc.cities.remote

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import pl.mc.cities.domain.City

class CityRemoteModelTest{
  @Test
  fun `test toCar data`() {
    //GIVEN
    val remoteModel = CityRemoteModel(
      cityName = "City",
      countryName = "Country",
      stateName = "State"
    )
    val expectedCity = City.create(
      cityName = "City",
      countryName = "Country",
      stateName = "State"
    )
    //WHEN
    val actual = remoteModel.toCity()
    //THEN
    assertThat(actual, equalTo(expectedCity))
  }

  @Test(expected = IllegalArgumentException::class)
  fun `should throw exception when cityName is null`() {
    CityRemoteModel(
      cityName = null,
      countryName = "Country",
      stateName = "State"
    ).toCity()
  }

  @Test(expected = IllegalArgumentException::class)
  fun `should throw exception when countryName is null`() {
    CityRemoteModel(
      cityName = "City",
      countryName = null,
      stateName = "State"
    ).toCity()
  }

  @Test(expected = IllegalArgumentException::class)
  fun `should throw exception when stateName is null`() {
    CityRemoteModel(
      cityName = "City",
      countryName = "Country",
      stateName = null
    ).toCity()
  }
}