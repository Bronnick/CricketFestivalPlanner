package com.cricketfest.cricketfestivalplanner.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColorTokens(
    val backgroundPage: Color,
    val backgroundCard: Color,
    val accent: Color,
    val accentSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color,
    val success: Color,
    val error: Color,
    val warning: Color,
    val info: Color,
    val onAccent: Color
)

val LightColorTokens = AppColorTokens(
    backgroundPage = Color(0xFFF9FAFB),
    backgroundCard = Color(0xFFFFFFFF),
    accent = Color(0xFF2176FF),
    accentSecondary = Color(0xFF4B9BFF),
    textPrimary = Color(0xFF1A1A1A),
    textSecondary = Color(0xFF4A4A4A),
    divider = Color(0xFFE3E8EF),
    success = Color(0xFF31C48D),
    error = Color(0xFFF05252),
    warning = Color(0xFFFACC15),
    info = Color(0xFF3B82F6),
    onAccent = Color(0xFFFFFFFF)
)

val DarkColorTokens = AppColorTokens(
    backgroundPage = Color(0xFF121212),
    backgroundCard = Color(0xFF1E1E1E),
    accent = Color(0xFF4B9BFF),
    accentSecondary = Color(0xFF2176FF),
    textPrimary = Color(0xFFF9FAFB),
    textSecondary = Color(0xFFB0BEC5),
    divider = Color(0xFF2C2C2C),
    success = Color(0xFF31C48D),
    error = Color(0xFFF05252),
    warning = Color(0xFFFACC15),
    info = Color(0xFF3B82F6),
    onAccent = Color(0xFFFFFFFF)
)

val LocalColorTokens = compositionLocalOf { LightColorTokens }
