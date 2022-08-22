package pl.mc.cities.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.intellij.lang.annotations.Language
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.mc.cities.HTTP_ERROR_CODES
import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.application.query.CitySearchQueryException
import pl.mc.cities.coAssertFail
import pl.mc.cities.createMockWebServer
import pl.mc.cities.createRetrofitService
import pl.mc.cities.domain.City
import java.net.HttpURLConnection

class RemoteCitySearchQueryIntegrationTest {

  private lateinit var mockServer: MockWebServer
  private val testDispatcher = StandardTestDispatcher()
  private val testScope = TestScope(testDispatcher)
  private lateinit var query: CitySearchQuery

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    mockServer = MockWebServer.createMockWebServer()
    val service = mockServer.createRetrofitService(CityService::class.java)
    query = RemoteCitySearchQuery(service)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    mockServer.shutdown()
  }

  @Test
  fun `should throw v when 400 and 500 errors occurs`() = testScope.runTest {
    HTTP_ERROR_CODES.forEach { errorCode ->
      //GIVEN
      mockServer.enqueue(MockResponse().apply {
        setResponseCode(errorCode)
      })
      //WHEN
      coAssertFail(CitySearchQueryException::class) {
        query.find("query")
      }
    }
  }

  @Test
  fun `should throw CitySearchQueryException when body is empty`() = testScope.runTest {
    //GIVEN
    mockServer.enqueue(MockResponse().apply {
      setResponseCode(HttpURLConnection.HTTP_OK)
    })
    //WHEN
    coAssertFail(CitySearchQueryException::class) {
      query.find("query")
    }
  }

  @Test
  fun `should return search result when JSON model is VALID`() = testScope.runTest {
    //GIVEN
    mockServer.enqueue(MockResponse().apply {
      setResponseCode(HttpURLConnection.HTTP_OK)
      setBody(VALID_JSON)
    })
    //WHEN
    val result = query.find("query")
    //THEN
    assertThat(result, equalTo(VALID_SEARCH_RESULT))
  }

  @Test
  fun `should filter out invalid cities when JSON is invalid`() = testScope.runTest {
    //GIVEN
    mockServer.enqueue(MockResponse().apply {
      setResponseCode(HttpURLConnection.HTTP_OK)
      setBody(INVALID_MODEL_JSON)
    })
    //WHEN
    val result = query.find("query")
    //THEN
    assertThat(result, equalTo(VALID_SEARCH_RESULT))
  }

  @Test(expected = CitySearchQueryException::class)
  fun `should throw CitySearchQueryException when JSON is malformed`() = testScope.runTest {
    //GIVEN
    mockServer.enqueue(MockResponse().apply {
      setResponseCode(HttpURLConnection.HTTP_OK)
      setBody(MALFORMED_MODEL_JSON)
    })
    //WHEN
    query.find("query")

  }
}

val VALID_SEARCH_RESULT = listOf(
  City.create(
    cityName = "San Francisco",
    stateName = "California",
    countryName = "United States"
  )
)

@Language("JSON")
val VALID_JSON = """
  {
    "geonames" : [
      {
        "toponymName" : "${VALID_SEARCH_RESULT[0].name}",
        "countryName" : "${VALID_SEARCH_RESULT[0].state.country.name}",
        "adminName1"   : "${VALID_SEARCH_RESULT[0].state.name}"
      }
    ]
  }
"""

/**
 * Wrong name of the field
 */
@Language("JSON")
val INVALID_MODEL_JSON = """
  {
    "geonames" : [
      {
        "wrongName" : "Los Angeles",
        "countryName" : "United States",
        "adminName1"   : "California"
      },
            {
        "toponymName" : "${VALID_SEARCH_RESULT[0].name}",
        "countryName" : "${VALID_SEARCH_RESULT[0].state.country.name}",
        "adminName1"   : "${VALID_SEARCH_RESULT[0].state.name}"
      }
    ]
  }
"""

/**
 * no comma between two cities
 */
@Language("JSON")
val MALFORMED_MODEL_JSON = """
  {
    "geonames" : [
      {
        "wrongName" : "Los Angeles",
        "countryName" : "United States",
        "adminName1"   : "California"
      }
      {
        "toponymName" : "${VALID_SEARCH_RESULT[0].name}",
        "countryName" : "${VALID_SEARCH_RESULT[0].state.country.name}",
        "adminName1"   : "${VALID_SEARCH_RESULT[0].state.name}"
      }
    ]
  }
"""