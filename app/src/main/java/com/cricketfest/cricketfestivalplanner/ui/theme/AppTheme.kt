package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(theme: String = "Light", content: @Composable () -> Unit) {
    val colorTokens = if (theme == "Dark") DarkColorTokens else LightColorTokens
    CompositionLocalProvider(
        LocalColorTokens provides colorTokens,
        LocalTypographyTokens provides DefaultTypographyTokens,
        LocalShapeTokens provides DefaultShapeTokens,
        LocalDimensionTokens provides DefaultDimensionTokens
    ) {
        content()
    }
}
