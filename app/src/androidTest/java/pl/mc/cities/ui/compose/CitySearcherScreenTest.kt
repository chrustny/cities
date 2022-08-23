package pl.mc.cities.ui.compose

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import pl.mc.cities.R
import pl.mc.cities.domain.City
import pl.mc.cities.theme.CityTheme
import pl.mc.cities.ui.CountrySearchViewModel

class CitySearcherScreenTest {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  private val viewModel = MockCountrySearchViewModel()

  @Test
  fun should_invoke_setQuery_when_user_click_search() {
    //GIVEN
    composeTestRule.setContent {
      CompositionLocalProvider {
        CityTheme {
          CitySearcherScreen(viewModel)
        }
      }
    }
    //WHEN
    composeTestRule.onNodeWithTag(TestTags.queryField).performTextInput("Warsaw")
    composeTestRule.onNodeWithTag(TestTags.queryButton).performClick()
    //THEN
    viewModel.verifySetQuery("Warsaw")
  }
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes res: Int) = this.activity.getString(res)

private class MockCountrySearchViewModel : CountrySearchViewModel {
  override val state: StateFlow<CountrySearchViewModel.State> = MutableStateFlow(
    CountrySearchViewModel.State.Available)
  override val searchQuery: StateFlow<String> = MutableStateFlow("")
  override val cities: StateFlow<List<City>> = MutableStateFlow(emptyList())

  var lastQuery : String? = null

  override fun setQuery(query: String) {
    lastQuery = query
  }

  fun verifySetQuery(expectedQuery: String){
    assertThat(lastQuery, equalTo(expectedQuery))
  }
}