package com.cricketfest.cricketfestivalplanner.ui.screens.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.data.model.StandingsEntry
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.EmptyStateView
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentTableScreen(tournamentId: Long, navController: NavController) {
    val viewModel: TournamentTableViewModel = koinViewModel(parameters = { parametersOf(tournamentId) })
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.standings_title), style = typo.headingMedium, color = colors.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.backgroundPage)
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> AppLoader()
            uiState.standings.isEmpty() -> EmptyStateView(
                message = stringResource(R.string.standings_empty),
                modifier = Modifier.padding(paddingValues)
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(horizontal = dimens.spacingMd)
            ) {
                Spacer(modifier = Modifier.height(dimens.spacingMd))
                // Header row
                StandingsHeaderRow()
                Divider(color = colors.divider, thickness = 1.dp)
                LazyColumn {
                    itemsIndexed(uiState.standings) { index, entry ->
                        StandingsRow(position = index + 1, entry = entry)
                        Divider(color = colors.divider, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StandingsHeaderRow() {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.spacingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "#", style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(0.5f))
        Text(text = stringResource(R.string.standings_team), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(3f))
        Text(text = stringResource(R.string.standings_played), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.standings_won), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.standings_lost), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.standings_points), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.standings_run_diff), style = typo.label, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

@Composable
private fun StandingsRow(position: Int, entry: StandingsEntry) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.spacingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$position", style = typo.bodyMedium, color = if (position <= 2) colors.accent else colors.textSecondary, modifier = Modifier.weight(0.5f))
        Text(text = entry.teamName, style = typo.bodyLarge, color = colors.textPrimary, modifier = Modifier.weight(3f))
        Text(text = "${entry.played}", style = typo.bodyMedium, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "${entry.won}", style = typo.bodyMedium, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "${entry.lost}", style = typo.bodyMedium, color = colors.textSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "${entry.points}", style = typo.bodyMedium, color = colors.accent, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        val rdColor = if (entry.runDifference >= 0) colors.success else colors.error
        Text(text = "${if (entry.runDifference > 0) "+" else ""}${entry.runDifference}", style = typo.bodyMedium, color = rdColor, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
}
