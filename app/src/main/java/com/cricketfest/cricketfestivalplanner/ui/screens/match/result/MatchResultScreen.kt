package com.cricketfest.cricketfestivalplanner.ui.screens.match.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.AppPrimaryButton
import com.cricketfest.cricketfestivalplanner.ui.components.AppTextField
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchResultScreen(matchId: Long, navController: NavController) {
    val viewModel: MatchResultViewModel = koinViewModel(parameters = { parametersOf(matchId) })
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) navController.popBackStack()
    }

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.match_result_title), style = typo.headingMedium, color = colors.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.backgroundPage)
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            AppLoader()
        } else {
            val match = uiState.match ?: return@Scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(dimens.spacingMd)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimens.spacingMd)
            ) {
                Text(
                    text = "${match.homeTeamName} ${stringResource(R.string.match_vs)} ${match.awayTeamName}",
                    style = typo.headingMedium,
                    color = colors.textPrimary
                )

                AppTextField(
                    value = uiState.homeScore,
                    onValueChange = { viewModel.updateHomeScore(it) },
                    label = stringResource(R.string.field_home_score, match.homeTeamName),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.homeScoreError,
                    errorMessage = if (uiState.homeScoreError) stringResource(R.string.error_score_invalid) else ""
                )

                AppTextField(
                    value = uiState.awayScore,
                    onValueChange = { viewModel.updateAwayScore(it) },
                    label = stringResource(R.string.field_away_score, match.awayTeamName),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.awayScoreError,
                    errorMessage = if (uiState.awayScoreError) stringResource(R.string.error_score_invalid) else ""
                )

                AppTextField(
                    value = uiState.bestPlayer,
                    onValueChange = { viewModel.updateBestPlayer(it) },
                    label = stringResource(R.string.field_best_player)
                )

                Spacer(modifier = Modifier.height(dimens.spacingSm))
                AppPrimaryButton(
                    text = stringResource(R.string.btn_save),
                    onClick = { viewModel.saveResult() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving
                )
            }
        }
    }
}
