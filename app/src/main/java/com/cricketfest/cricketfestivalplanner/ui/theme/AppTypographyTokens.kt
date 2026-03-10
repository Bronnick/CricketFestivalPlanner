package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class AppTypographyTokens(
    val headingLarge: TextStyle,
    val headingMedium: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val caption: TextStyle,
    val label: TextStyle
)

val DefaultTypographyTokens = AppTypographyTokens(
    headingLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
    headingMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    caption = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
    label = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
)

val LocalTypographyTokens = compositionLocalOf { DefaultTypographyTokens }
