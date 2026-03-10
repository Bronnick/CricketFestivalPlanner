package com.cricketfest.cricketfestivalplanner.ui.screens.home

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val organizerName by viewModel.organizerName.collectAsState()
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
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.home_quick_create))
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
                text = if (organizerName.isNotBlank())
                    stringResource(R.string.home_welcome_name, organizerName)
                else
                    stringResource(R.string.home_welcome),
                style = typo.headingLarge,
                color = colors.textPrimary
            )
            Text(
                text = stringResource(R.string.home_subtitle),
                style = typo.bodyMedium,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(dimens.spacingLg))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_active_tournaments),
                    style = typo.headingMedium,
                    color = colors.textPrimary
                )
                TextButton(onClick = { navController.navigate(Routes.TournamentList.route) }) {
                    Text(
                        text = stringResource(R.string.home_view_all),
                        style = typo.label,
                        color = colors.accent
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.spacingSm))

            when {
                uiState.isLoading -> AppLoader()
                uiState.error != null -> EmptyStateView(message = stringResource(R.string.error_generic))
                uiState.activeTournaments.isEmpty() -> EmptyStateView(
                    message = stringResource(R.string.home_empty)
                )
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimens.spacingSm),
                    contentPadding = PaddingValues(bottom = dimens.spacingXl)
                ) {
                    items(uiState.activeTournaments) { tournament ->
                        TournamentCard(tournament = tournament) {
                            navController.navigate(Routes.TournamentDetail.createRoute(tournament.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentCard(tournament: Tournament, onClick: () -> Unit) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    AppCard(onClick = onClick) {
        Text(text = tournament.name, style = typo.headingMedium, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(dimens.spacingXs))
        Text(
            text = "${tournament.matchType} • ${tournament.structure} • ${tournament.teamCount} teams",
            style = typo.bodyMedium,
            color = colors.textSecondary
        )
        if (tournament.location.isNotBlank()) {
            Text(text = tournament.location, style = typo.caption, color = colors.textSecondary)
        }
    }
}
