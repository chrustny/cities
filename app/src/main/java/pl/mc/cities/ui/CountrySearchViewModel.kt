package pl.mc.cities.ui

import kotlinx.coroutines.flow.StateFlow
import pl.mc.cities.domain.City

interface CountrySearchViewModel {
  val state: StateFlow<State>
  val searchQuery: StateFlow<String>
  val cities: StateFlow<List<City>>
  fun setQuery(query: String)

  sealed interface State {
    object Loading : State
    object Available : State
    class Error : State
  }
}