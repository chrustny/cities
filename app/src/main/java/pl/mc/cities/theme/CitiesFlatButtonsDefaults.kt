package pl.mc.cities.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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