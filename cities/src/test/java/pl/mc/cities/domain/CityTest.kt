package pl.mc.cities.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class CityTest {
  @Test
  fun `test City factory method`() {
    //GIVEN
    val expectedCity = City(
      name = "Los Angeles",
      state = State(
        name = "California",
        country = Country(name = "United States")
      )
    )
    //WHEN
    val actual = City.create(
      cityName = "Los Angeles",
      stateName = "California",
      countryName = "United States"
    )
    //THEN
    assertThat(actual, equalTo(expectedCity))
  }
}