package com.cricketfest.cricketfestivalplanner.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.BottomNavBar
import com.cricketfest.cricketfestivalplanner.ui.components.EmptyStateView
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsScreen(navController: NavController) {
    val viewModel: AnalyticsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        when {
            uiState.isLoading -> AppLoader()
            uiState.tournamentStats.isEmpty() -> EmptyStateView(
                message = stringResource(R.string.analytics_empty),
                modifier = Modifier.padding(paddingValues)
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(dimens.spacingMd)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimens.spacingMd)
            ) {
                Text(text = stringResource(R.string.analytics_title), style = typo.headingLarge, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(dimens.spacingXs))

                // Global summary cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.spacingMd)
                ) {
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text(text = "${uiState.totalTournaments}", style = typo.headingLarge, color = colors.accent)
                        Text(text = stringResource(R.string.analytics_total_tournaments), style = typo.caption, color = colors.textSecondary)
                    }
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text(text = "${uiState.totalMatchesPlayed}", style = typo.headingLarge, color = colors.accent)
                        Text(text = stringResource(R.string.analytics_total_matches), style = typo.caption, color = colors.textSecondary)
                    }
                }

                if (uiState.topTeam.isNotBlank()) {
                    AppCard {
                        Text(text = stringResource(R.string.analytics_top_team), style = typo.caption, color = colors.textSecondary)
                        Text(text = uiState.topTeam, style = typo.headingMedium, color = colors.accent)
                    }
                }

                // Per-tournament breakdown
                Text(text = stringResource(R.string.analytics_breakdown_title), style = typo.headingMedium, color = colors.textPrimary)

                uiState.tournamentStats.forEach { stats ->
                    AppCard {
                        Text(text = stats.tournament.name, style = typo.bodyLarge, color = colors.textPrimary)
                        Text(
                            text = "${stats.tournament.matchType} • ${stats.tournament.structure}",
                            style = typo.caption,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(dimens.spacingSm))
                        Divider(color = colors.divider)
                        Spacer(modifier = Modifier.height(dimens.spacingSm))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${stats.matchesPlayed}", style = typo.headingMedium, color = colors.accent)
                                Text(text = stringResource(R.string.analytics_matches_played), style = typo.caption, color = colors.textSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${stats.totalMatches}", style = typo.headingMedium, color = colors.textPrimary)
                                Text(text = stringResource(R.string.analytics_matches_total), style = typo.caption, color = colors.textSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val pct = if (stats.totalMatches > 0) stats.matchesPlayed * 100 / stats.totalMatches else 0
                                Text(text = "$pct%", style = typo.headingMedium, color = colors.success)
                                Text(text = stringResource(R.string.analytics_completion), style = typo.caption, color = colors.textSecondary)
                            }
                        }
                        if (stats.totalMatches > 0) {
                            Spacer(modifier = Modifier.height(dimens.spacingSm))
                            LinearProgressIndicator(
                                progress = { stats.matchesPlayed.toFloat() / stats.totalMatches },
                                modifier = Modifier.fillMaxWidth(),
                                color = colors.accent,
                                trackColor = colors.divider
                            )
                        }
                        if (stats.topTeam.isNotBlank()) {
                            Spacer(modifier = Modifier.height(dimens.spacingSm))
                            Text(
                                text = stringResource(R.string.analytics_top_team) + ": " + stats.topTeam,
                                style = typo.caption,
                                color = colors.textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimens.spacingXl))
            }
        }
    }
}
