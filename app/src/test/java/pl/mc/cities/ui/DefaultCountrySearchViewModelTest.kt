package pl.mc.cities.ui

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.application.query.CitySearchQueryException
import pl.mc.cities.domain.City

class DefaultCountrySearchViewModelTest {

  private val testDispatcher = StandardTestDispatcher()
  private val testScope = TestScope(testDispatcher)
  private val citySearchQuery = MockCitySearchQuery()
  private val viewModel = DefaultCountrySearchViewModel(citySearchQuery)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `should emit Idle state when no search query`() = testScope.runTest {
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Idle))
  }

  @Test
  fun `should emit query when query typed`() = testScope.runTest {
    //GIVEN
    val query = "Warsa"
    //WHEN
    viewModel.setQuery(query)
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.searchQuery.value, equalTo(query))

  }

  @Test
  fun `should emit Error state when Query throw exception`() = testScope.runTest {
    //WHEN
    viewModel.setQuery("Warsa")
    citySearchQuery.onError(CitySearchQueryException())
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Error))
  }

  @Test
  fun `should emit Loading state when Query is in progress`() = testScope.runTest {
    //WHEN
    viewModel.setQuery("Warsa")
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Loading))
  }

  @Test
  fun `should emit Available state when empty results fetched with success`() = testScope.runTest {
    //WHEN
    viewModel.setQuery("Warsa")
    advanceUntilIdle()
    citySearchQuery.onSuccess(emptyList())
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Available))
  }

  private val cityWarsaw = City.create(
    cityName = "Warsaw",
    stateName = "Masovian",
    countryName = "Poland"
  )

  @Test
  fun `should emit Available state when results fetched with success`() = testScope.runTest {
    //WHEN
    viewModel.setQuery("Warsa")
    advanceUntilIdle()
    citySearchQuery.onSuccess(listOf(cityWarsaw))
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Available))
    assertThat(viewModel.cities.value, equalTo(listOf(cityWarsaw)))
  }

   private val unsortedCities = listOf(
     City.create(cityName = "Warsaw", stateName = "Masovian", countryName = "Poland"),
     City.create(cityName = "Płońsk", stateName = "Masovian", countryName = "Poland"),
     City.create(cityName = "Los Angeles", stateName = "California", countryName = "United States"),
     City.create(cityName = "Berlin", stateName = "Berlin", countryName = "Germany"),
     City.create(cityName = "München ", stateName = "Bavaria", countryName = "Germany"),
     City.create(cityName = "San Diego", stateName = "California", countryName = "United States"),
     City.create(cityName = "Huntsville", stateName = "Alabama", countryName = "United States"),
     City.create(cityName = "Birmingham", stateName = "Alabama", countryName = "United States")
   )

  private val sortedCities = listOf(
    City.create(cityName = "Birmingham", stateName = "Alabama", countryName = "United States"),
    City.create(cityName = "Huntsville", stateName = "Alabama", countryName = "United States"),
    City.create(cityName = "Los Angeles", stateName = "California", countryName = "United States"),
    City.create(cityName = "San Diego", stateName = "California", countryName = "United States"),
    City.create(cityName = "München ", stateName = "Bavaria", countryName = "Germany"),
    City.create(cityName = "Berlin", stateName = "Berlin", countryName = "Germany"),
    City.create(cityName = "Płońsk", stateName = "Masovian", countryName = "Poland"),
    City.create(cityName = "Warsaw", stateName = "Masovian", countryName = "Poland"),
  )

  @Test
  fun `should emit cities sorted with preferred country`() = testScope.runTest {
    //WHEN
    viewModel.setQuery("Warsa")
    advanceUntilIdle()
    citySearchQuery.onSuccess(unsortedCities)
    advanceUntilIdle()
    //THEN
    assertThat(viewModel.state.value, equalTo(CountrySearchViewModel.State.Available))
    assertThat(viewModel.cities.value, equalTo(sortedCities))
  }
}

class MockCitySearchQuery : CitySearchQuery {
  private var completableDeferred = CompletableDeferred<Unit>()
  private var answer: (() -> List<City>)? = null

  fun onSuccess(response: List<City>) {
    answer = { response }
    completableDeferred.complete(Unit)
  }

  fun onError(error: Throwable) {
    answer = { throw error }
    completableDeferred.complete(Unit)
  }

  override suspend fun find(query: String): List<City> {
    completableDeferred.await()
    return answer?.invoke() ?: throw IllegalStateException("Answer not set")
  }
}