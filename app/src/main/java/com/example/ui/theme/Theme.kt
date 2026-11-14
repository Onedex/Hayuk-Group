package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val HayukLightColorScheme = lightColorScheme(
    primary = HayukRed,
    onPrimary = PureWhite,
    primaryContainer = HayukRedSoft,
    onPrimaryContainer = HayukRedDeep,
    secondary = CarbonBlack,
    onSecondary = PureWhite,
    secondaryContainer = WarmCreamSurface,
    onSecondaryContainer = CarbonBlack,
    tertiary = HayukLogoGold,
    onTertiary = CarbonBlack,
    background = PureWhite,
    onBackground = CarbonBlack,
    surface = PureWhite,
    onSurface = CarbonBlack,
    surfaceVariant = SnowSurface,
    onSurfaceVariant = SlateText,
    outline = HairlineBorder,
    outlineVariant = HayukRedBorder
)

private val HayukDarkColorScheme = darkColorScheme(
    primary = HayukRed,
    onPrimary = PureWhite,
    primaryContainer = HayukRedDark,
    onPrimaryContainer = PureWhite,
    secondary = PureWhite,
    onSecondary = CarbonBlack,
    background = CarbonBlack,
    onBackground = PureWhite,
    surface = CharcoalSurface,
    onSurface = PureWhite,
    surfaceVariant = CharcoalSurface,
    onSurfaceVariant = MutedGray,
    outline = CharcoalSurface
)

val HayukShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun HayukTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) HayukDarkColorScheme else HayukLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HayukTypography,
        shapes = HayukShapes,
        content = content
    )
}
