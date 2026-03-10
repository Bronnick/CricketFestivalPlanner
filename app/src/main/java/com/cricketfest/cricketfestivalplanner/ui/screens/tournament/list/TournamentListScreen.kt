package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.list

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.BottomNavBar
import com.cricketfest.cricketfestivalplanner.ui.components.EmptyStateView
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TournamentListScreen(navController: NavController) {
    val viewModel: TournamentListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.CreateTournament.route) },
                containerColor = colors.accent,
                contentColor = colors.onAccent
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.btn_create))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPage)
                .padding(paddingValues)
                .padding(dimens.spacingMd)
        ) {
            Text(
                text = stringResource(R.string.tournament_list_title),
                style = typo.headingLarge,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(dimens.spacingMd))

            // Filter chips
            Row(horizontalArrangement = Arrangement.spacedBy(dimens.spacingSm)) {
                TournamentFilter.values().forEach { filter ->
                    val labelRes = when (filter) {
                        TournamentFilter.ALL -> R.string.tournament_filter_all
                        TournamentFilter.ACTIVE -> R.string.tournament_filter_active
                        TournamentFilter.COMPLETED -> R.string.tournament_filter_completed
                    }
                    FilterChip(
                        selected = uiState.filter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text(text = stringResource(labelRes), style = typo.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.accent,
                            selectedLabelColor = colors.onAccent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.spacingMd))

            when {
                uiState.isLoading -> AppLoader()
                uiState.error != null -> EmptyStateView(message = stringResource(R.string.error_generic))
                uiState.tournaments.isEmpty() -> EmptyStateView(message = stringResource(R.string.tournament_list_empty))
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimens.spacingSm),
                    contentPadding = PaddingValues(bottom = dimens.spacingXl)
                ) {
                    items(uiState.tournaments) { tournament ->
                        TournamentListItem(tournament = tournament) {
                            navController.navigate(Routes.TournamentDetail.createRoute(tournament.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentListItem(tournament: Tournament, onClick: () -> Unit) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    AppCard(onClick = onClick) {
        Text(text = tournament.name, style = typo.headingMedium, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(dimens.spacingXs))
        Text(
            text = "${tournament.matchType} • ${tournament.structure}",
            style = typo.bodyMedium,
            color = colors.textSecondary
        )
        Text(
            text = "${tournament.teamCount} teams",
            style = typo.caption,
            color = colors.textSecondary
        )
    }
}
