package pl.mc.cities.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
  val results by viewModel.cities.collectAsState()

  var pendingQuery by rememberSaveable { mutableStateOf(query) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Search") }
      )
    }
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Row {
        TextField(
          value = pendingQuery,
          onValueChange = { pendingQuery = it },
          singleLine = true,
          colors = TextFieldDefaults.outlinedTextFieldColors(),
          modifier = Modifier
            .weight(1f)
            .align(CenterVertically)
        )
        Button(
          onClick = { viewModel.setQuery(pendingQuery) },
          colors = CitiesFlatButtonsDefaults.buttonColors(),
          elevation = CitiesFlatButtonsDefaults.elevation(),
          modifier = Modifier.height(64.dp)
        ) {
          Text(
            text = "Search",
            modifier = Modifier.clickable { viewModel.setQuery(pendingQuery) }
          )
        }
      }
      LazyColumn {
        items(results) {
          CityItem(city = it)
        }
      }
    }
  }
}

@Preview
@Composable
fun CitySearcherScreenPreview() {
  CityTheme {
    CitySearcherScreen(
      MockCountrySearchViewModel(
        listOf(
          City.create(
            cityName = "San Diego",
            stateName = "California",
            countryName = "United States"
          ),
          City.create("Warsaw", stateName = "Masovian", countryName = "Poland")
        )
      )
    )
  }
}

private class MockCountrySearchViewModel(
  citiesList: List<City>
) : CountrySearchViewModel {
  override val state: StateFlow<State> = MutableStateFlow(State.Available)
  override val searchQuery: StateFlow<String> = MutableStateFlow("query")
  override val cities: StateFlow<List<City>> = MutableStateFlow(citiesList)

  override fun setQuery(query: String) {
    //no-op
  }

}


object CitiesFlatButtonsDefaults {

  @Composable
  fun buttonColors(
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    disabledBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
      .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = MaterialTheme.colors.onSurface
      .copy(alpha = ContentAlpha.disabled)
  ): ButtonColors = ButtonDefaults.buttonColors(
    backgroundColor,
    contentColor,
    disabledBackgroundColor,
    disabledContentColor
  )

  @Composable
  fun elevation(
    defaultElevation: Dp = 0.dp,
    pressedElevation: Dp = 0.dp,
    disabledElevation: Dp = 0.dp,
    hoveredElevation: Dp = 0.dp,
    focusedElevation: Dp = 0.dp,
  ): ButtonElevation {
    return ButtonDefaults.elevation(
      defaultElevation,
      pressedElevation,
      disabledElevation,
      hoveredElevation,
      focusedElevation
    )
  }
}