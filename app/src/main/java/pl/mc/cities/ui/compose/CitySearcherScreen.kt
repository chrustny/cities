package pl.mc.cities.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pl.mc.cities.domain.City
import pl.mc.cities.theme.CitiesFlatButtonsDefaults
import pl.mc.cities.theme.CityTheme
import pl.mc.cities.ui.CountrySearchViewModel
import pl.mc.cities.ui.CountrySearchViewModel.State

@Composable
fun CitySearcherScreen(viewModel: CountrySearchViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Search") }
      )
    }
  ) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
      if (this.maxWidth < 500.dp) {
        Content(viewModel)
      } else {
        Box(
          Modifier
            .width(500.dp)
            .align(TopCenter)
        ) {
          Content(viewModel)
        }
      }
    }
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Content(viewModel: CountrySearchViewModel) {
  val query by viewModel.searchQuery.collectAsState()
  val results by viewModel.cities.collectAsState()

  var pendingQuery by rememberSaveable { mutableStateOf(query) }
  val keyboardController = LocalSoftwareKeyboardController.current


  Column(modifier = Modifier.padding(8.dp)) {
    Row {
      TextField(
        value = pendingQuery,
        onValueChange = { pendingQuery = it },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(),
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            viewModel.setQuery(pendingQuery)
            keyboardController?.hide()
          }
        ),
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

@Preview(widthDp = 360)
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

@Preview(widthDp = 800, heightDp = 400)
@Composable
fun CitySearcherScreenPreviewTablet() {
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