package pl.mc.cities.remote

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import pl.mc.cities.application.query.CitySearchQueryException
import pl.mc.cities.domain.City
import retrofit2.HttpException
import retrofit2.Response


class RemoteCitySearchQueryTest {
  private val service: CityService = mockk(relaxed = true)
  private val remoteQuery = RemoteCitySearchQuery(service)
  private val scope = TestScope()

  @Test(expected = CitySearchQueryException::class)
  fun `should return CitySearchQueryException when any http error occurs`() = scope.runTest {
    //GIVEN
    coEvery { service.queryCities(any()) } throws createHttpError(404)
    //WHEN
    remoteQuery.find("Ca")
  }

  @Test
  fun `should return list of cities only with non null fields`() = scope.runTest {
    //GIVEN
    val cities = listOf(
      CityRemoteModel("Los Angeles", "United States", "California"),
      CityRemoteModel("A", null, "C"),
      CityRemoteModel(null, "B", "C"),
      CityRemoteModel("A", "B", null),
    )
    val expectedCity = City.create("Los Angeles", "California", "United States")

    coEvery { service.queryCities(any()) } returns CitiesResponseRemoteModel(cities)
    //WHEN
    val foundCities = remoteQuery.find("some")
    //THEN
    assertThat(foundCities, hasSize(1))
    assertThat(foundCities[0], equalTo(expectedCity))
  }

}

fun createHttpError(code: Int): HttpException {
  val response = Response.error<String>(code, "".toResponseBody())
  return HttpException(response)
}