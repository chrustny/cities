package pl.mc.cities.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun CityTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = CityMaterialLightColors,
    content = content,
  )
}

private val CityMaterialLightColors = lightColors(
  onSurface = Color(0xFF282622),
  background = Color(0xFFFAF7F2),
  primary = Color(0xFF6495ED)
)