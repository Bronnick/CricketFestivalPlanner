package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class AppShapeTokens(
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val button: Shape,
    val card: Shape
)

val DefaultShapeTokens = AppShapeTokens(
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp),
    button = RoundedCornerShape(12.dp),
    card = RoundedCornerShape(12.dp)
)

val LocalShapeTokens = compositionLocalOf { DefaultShapeTokens }
