package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.EmptyStateView
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentHistoryScreen(navController: NavController) {
    val viewModel: TournamentHistoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.history_title), style = typo.headingMedium, color = colors.textPrimary) },
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
            uiState.tournaments.isEmpty() -> EmptyStateView(
                message = stringResource(R.string.history_empty),
                modifier = Modifier.padding(paddingValues)
            )
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(horizontal = dimens.spacingMd),
                verticalArrangement = Arrangement.spacedBy(dimens.spacingSm),
                contentPadding = PaddingValues(vertical = dimens.spacingMd)
            ) {
                items(uiState.tournaments) { tournament ->
                    HistoryItem(tournament = tournament,
                        onView = { navController.navigate(Routes.TournamentDetail.createRoute(tournament.id)) },
                        onReuse = { navController.navigate(Routes.CreateTournamentFromTemplate.createRoute(tournament.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    tournament: Tournament,
    onView: () -> Unit,
    onReuse: () -> Unit
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    AppCard(onClick = onView) {
        Text(text = tournament.name, style = typo.headingMedium, color = colors.textPrimary)
        Spacer(modifier = Modifier.height(dimens.spacingXs))
        Text(
            text = "${tournament.matchType} • ${tournament.structure} • ${tournament.teamCount} teams",
            style = typo.bodyMedium,
            color = colors.textSecondary
        )
        TextButton(onClick = onReuse) {
            Text(text = stringResource(R.string.history_reuse), style = typo.label, color = colors.accent)
        }
    }
}
