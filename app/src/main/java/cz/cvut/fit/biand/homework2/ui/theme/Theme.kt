package cz.cvut.fit.biand.homework2.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColors = lightColors(
    primary = Primary,
    primaryVariant = Blue,
    secondary = Color.White,
    onPrimary = Color.Black,
)

@Composable
fun Homework2Theme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = lightColors,
        content = content,
    )
}
