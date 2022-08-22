package pl.mc.cities.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.mc.cities.domain.City

@Composable
fun CityItem(city: City) {
  Row(modifier = Modifier.padding(8.dp)) {
    Column(
      Modifier
        .weight(1f)) {
      Text(
        text = city.name,
        style = MaterialTheme.typography.h6
      )
      Text(
        text = city.state.name,
        style = MaterialTheme.typography.subtitle1
      )
    }
    Text(
      text = city.state.country.name,
      style = MaterialTheme.typography.subtitle1,
      modifier = Modifier.align(Alignment.CenterVertically)
    )
  }

}


@Preview(widthDp = 320)
@Composable
fun CityItemPreview() {
  CityItem(
    City.create(
      cityName = "Los Angeles",
      stateName = "California",
      countryName = "United States"
    )
  )
}