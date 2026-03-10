package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensionTokens(
    val spacingXs: Dp,
    val spacingSm: Dp,
    val spacingMd: Dp,
    val spacingLg: Dp,
    val spacingXl: Dp,
    val elevationCard: Dp,
    val elevationModal: Dp,
    val elevationFab: Dp
)

val DefaultDimensionTokens = AppDimensionTokens(
    spacingXs = 4.dp,
    spacingSm = 8.dp,
    spacingMd = 16.dp,
    spacingLg = 24.dp,
    spacingXl = 32.dp,
    elevationCard = 2.dp,
    elevationModal = 8.dp,
    elevationFab = 12.dp
)

val LocalDimensionTokens = compositionLocalOf { DefaultDimensionTokens }
