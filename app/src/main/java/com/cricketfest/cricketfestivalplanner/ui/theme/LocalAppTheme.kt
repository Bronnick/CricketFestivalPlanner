package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object LocalAppTheme {
    val colors: AppColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalColorTokens.current

    val typography: AppTypographyTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalTypographyTokens.current

    val shapes: AppShapeTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalShapeTokens.current

    val dimens: AppDimensionTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensionTokens.current
}
