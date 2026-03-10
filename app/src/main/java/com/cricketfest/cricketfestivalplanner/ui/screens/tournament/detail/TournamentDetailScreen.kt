package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.detail

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.AppOutlinedButton
import com.cricketfest.cricketfestivalplanner.ui.components.AppPrimaryButton
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(tournamentId: Long, navController: NavController) {
    val viewModel: TournamentDetailViewModel = koinViewModel(parameters = { parametersOf(tournamentId) })
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    LaunchedEffect(uiState.completed) {
        if (uiState.completed) navController.popBackStack()
    }

    if (uiState.showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCompleteDialog() },
            title = { Text(text = stringResource(R.string.tournament_complete_title), style = typo.headingMedium) },
            text = { Text(text = stringResource(R.string.tournament_complete_confirm), style = typo.bodyMedium) },
            confirmButton = {
                TextButton(onClick = { viewModel.markAsCompleted() }) {
                    Text(text = stringResource(R.string.dialog_confirm), color = colors.accent)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissCompleteDialog() }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = colors.textSecondary)
                }
            },
            containerColor = colors.backgroundCard
        )
    }

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.tournament?.name ?: "",
                        style = typo.headingMedium,
                        color = colors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                    }
                },
                actions = {
                    if (uiState.tournament?.isCompleted == false) {
                        IconButton(onClick = {
                            navController.navigate(Routes.EditTournament.createRoute(tournamentId))
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.tournament_detail_edit), tint = colors.accent)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.backgroundPage)
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            AppLoader()
        } else {
            val tournament = uiState.tournament ?: return@Scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(dimens.spacingMd)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimens.spacingMd)
            ) {
                // Status badge if completed
                if (tournament.isCompleted) {
                    Text(
                        text = stringResource(R.string.tournament_filter_completed),
                        style = typo.label,
                        color = colors.success
                    )
                }

                // Info card
                AppCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = stringResource(R.string.field_match_type), style = typo.caption, color = colors.textSecondary)
                            Text(text = tournament.matchType, style = typo.bodyLarge, color = colors.textPrimary)
                        }
                        Column {
                            Text(text = stringResource(R.string.tournament_detail_structure), style = typo.caption, color = colors.textSecondary)
                            Text(text = tournament.structure, style = typo.bodyLarge, color = colors.textPrimary)
                        }
                        Column {
                            Text(text = stringResource(R.string.field_team_count), style = typo.caption, color = colors.textSecondary)
                            Text(text = "${tournament.teamCount}", style = typo.bodyLarge, color = colors.textPrimary)
                        }
                    }
                    if (tournament.location.isNotBlank()) {
                        Spacer(modifier = Modifier.height(dimens.spacingSm))
                        Text(text = stringResource(R.string.tournament_detail_location), style = typo.caption, color = colors.textSecondary)
                        Text(text = tournament.location, style = typo.bodyLarge, color = colors.textPrimary)
                    }
                }

                // Action buttons
                AppPrimaryButton(
                    text = stringResource(R.string.tournament_detail_schedule),
                    onClick = { navController.navigate(Routes.MatchSchedule.createRoute(tournamentId)) },
                    modifier = Modifier.fillMaxWidth()
                )
                AppPrimaryButton(
                    text = stringResource(R.string.tournament_detail_table),
                    onClick = { navController.navigate(Routes.TournamentTable.createRoute(tournamentId)) },
                    modifier = Modifier.fillMaxWidth()
                )
                AppPrimaryButton(
                    text = stringResource(R.string.tournament_detail_analytics),
                    onClick = { navController.navigate(Routes.Analytics.route) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (!tournament.isCompleted) {
                    Spacer(modifier = Modifier.height(dimens.spacingSm))
                    AppOutlinedButton(
                        text = stringResource(R.string.tournament_complete),
                        onClick = { viewModel.showCompleteDialog() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
