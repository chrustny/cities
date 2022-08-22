package pl.mc.cities.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.application.query.CitySearchQueryException
import pl.mc.cities.domain.City
import pl.mc.cities.domain.Country
import pl.mc.cities.domain.CountryPriorityCityComparator
import javax.inject.Inject
import pl.mc.cities.ui.CountrySearchViewModel.*

@HiltViewModel
class DefaultCountrySearchViewModel @Inject constructor(
  private val citySearchQuery: CitySearchQuery
) : ViewModel(), CountrySearchViewModel {

  private var _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
  override val state: StateFlow<State> = _state

  private var _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
  override val searchQuery: StateFlow<String> = _searchQuery

  private var _cities: MutableStateFlow<List<City>> = MutableStateFlow(emptyList())
  override val cities: StateFlow<List<City>> = _cities

  private var searchJob: Job? = null

  override fun setQuery(query: String) {
    _searchQuery.value = query
    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      performSearch(query)
    }
  }

  private suspend fun performSearch(query: String) {
    _state.value = State.Loading
    try {
      _cities.value = citySearchQuery.find(query)
        .sortedWith(CountryPriorityCityComparator(Country("United States")).reversed())
      _state.value = State.Available
    } catch (queryException: CitySearchQueryException) {
      _state.value = State.Error()
    }
  }
}
