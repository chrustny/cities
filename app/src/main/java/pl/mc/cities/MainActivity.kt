package pl.mc.cities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pl.mc.cities.theme.CityTheme
import pl.mc.cities.ui.DefaultCountrySearchViewModel
import pl.mc.cities.ui.compose.CitySearcherScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private val countrySearchViewModel: DefaultCountrySearchViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CityTheme {
        CitySearcherScreen(countrySearchViewModel)
      }
    }
  }
}