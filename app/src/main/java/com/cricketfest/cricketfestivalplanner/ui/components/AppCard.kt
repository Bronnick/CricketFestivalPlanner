package com.cricketfest.cricketfestivalplanner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppTheme.colors
    val shapes = LocalAppTheme.shapes
    val dimens = LocalAppTheme.dimens

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shapes.card,
            colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationCard)
        ) {
            Column(modifier = Modifier.padding(dimens.spacingMd), content = content)
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shapes.card,
            colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationCard)
        ) {
            Column(modifier = Modifier.padding(dimens.spacingMd), content = content)
        }
    }
}
