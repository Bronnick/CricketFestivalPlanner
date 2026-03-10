package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColorTokens provides LightColorTokens,
        LocalTypographyTokens provides DefaultTypographyTokens,
        LocalShapeTokens provides DefaultShapeTokens,
        LocalDimensionTokens provides DefaultDimensionTokens
    ) {
        content()
    }
}
