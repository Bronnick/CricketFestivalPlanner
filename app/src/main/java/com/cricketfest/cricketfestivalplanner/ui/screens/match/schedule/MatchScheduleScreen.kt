package com.cricketfest.cricketfestivalplanner.ui.screens.match.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.EmptyStateView
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import com.cricketfest.cricketfestivalplanner.utils.DateUtils
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScheduleScreen(tournamentId: Long, navController: NavController) {
    val viewModel: MatchScheduleViewModel = koinViewModel(parameters = { parametersOf(tournamentId) })
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.match_schedule_title), style = typo.headingMedium, color = colors.textPrimary) },
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
            uiState.matches.isEmpty() -> EmptyStateView(
                message = stringResource(R.string.match_schedule_empty),
                modifier = Modifier.padding(paddingValues)
            )
            else -> {
                val grouped = uiState.matches.groupBy { it.round }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.backgroundPage)
                        .padding(paddingValues)
                        .padding(horizontal = dimens.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(dimens.spacingSm),
                    contentPadding = PaddingValues(vertical = dimens.spacingMd)
                ) {
                    grouped.entries.sortedBy { it.key }.forEach { (round, matches) ->
                        item {
                            Text(
                                text = stringResource(R.string.match_round, round),
                                style = typo.headingMedium,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(dimens.spacingXs))
                        }
                        items(matches) { match ->
                            MatchItem(match = match) {
                                if (!match.isCompleted) {
                                    navController.navigate(Routes.MatchResult.createRoute(match.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchItem(match: Match, onEnterResult: () -> Unit) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${match.homeTeamName} ${stringResource(R.string.match_vs)} ${match.awayTeamName}",
                    style = typo.bodyLarge,
                    color = colors.textPrimary
                )
                Text(
                    text = stringResource(R.string.match_time, DateUtils.formatDateTime(match.scheduledTime)),
                    style = typo.caption,
                    color = colors.textSecondary
                )
            }
            if (match.isCompleted) {
                Text(
                    text = stringResource(R.string.match_completed),
                    style = typo.label,
                    color = colors.success
                )
            } else {
                TextButton(onClick = onEnterResult) {
                    Text(
                        text = stringResource(R.string.match_enter_result),
                        style = typo.label,
                        color = colors.accent
                    )
                }
            }
        }
    }
}
