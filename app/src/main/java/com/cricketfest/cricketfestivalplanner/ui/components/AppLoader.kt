package com.cricketfest.cricketfestivalplanner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

@Composable
fun AppLoader(modifier: Modifier = Modifier) {
    val colors = LocalAppTheme.colors
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = colors.accent,
            strokeWidth = 4.dp
        )
    }
}
