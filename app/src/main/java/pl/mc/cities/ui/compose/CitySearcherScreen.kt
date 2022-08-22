package pl.mc.cities.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pl.mc.cities.domain.City
import pl.mc.cities.theme.CityTheme
import pl.mc.cities.ui.CountrySearchViewModel
import pl.mc.cities.ui.CountrySearchViewModel.State

@Composable
fun CitySearcherScreen(viewModel: CountrySearchViewModel) {
  val query by viewModel.searchQuery.collectAsState()
  var pendingQuery by remember { mutableStateOf(query) }
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Search") },
      )
    }
  ) {
    Row {
      TextField(value = query, onValueChange = { pendingQuery = it })
      Button(onClick = { viewModel.setQuery(pendingQuery) }) {
        Text("Search")
      }
    }

  }
}

@Composable
fun CityItem(city: City) {
  Row {
    Column(Modifier.padding(8.dp)) {
      Text(text = city.name)
      Text(text = city.state.name)
    }
    Text(text = city.state.country.name)
  }

}


@Preview
@Composable
fun CityItemPreview() {
  CityItem(City.create("Los Angeles", "California", "United States"))
}

@Preview
@Composable
fun CitySearcherScreenPreview() {
  CityTheme {
    CitySearcherScreen(MockCountrySearchViewModel())
  }
}

private class MockCountrySearchViewModel : CountrySearchViewModel {
  override val state: StateFlow<State> = MutableStateFlow(State.Available)
  override val searchQuery: StateFlow<String> = MutableStateFlow("query")
  override val cities: StateFlow<List<City>> = MutableStateFlow(emptyList())

  override fun setQuery(query: String) {
    //no-op
  }

}