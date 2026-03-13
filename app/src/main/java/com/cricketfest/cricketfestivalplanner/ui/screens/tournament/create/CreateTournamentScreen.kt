package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.create

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppPrimaryButton
import com.cricketfest.cricketfestivalplanner.ui.components.AppTextField
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTournamentScreen(navController: NavController, templateId: Long? = null) {
    val viewModel: CreateTournamentViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(templateId) {
        if (templateId != null) viewModel.loadTemplate(templateId)
    }
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    LaunchedEffect(uiState.savedTournamentId) {
        uiState.savedTournamentId?.let { id ->
            navController.navigate(Routes.MatchSchedule.createRoute(id)) {
                popUpTo(Routes.CreateTournament.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_tournament_title), style = typo.headingMedium, color = colors.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.backgroundPage)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPage)
                .padding(paddingValues)
                .padding(dimens.spacingMd)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimens.spacingMd)
        ) {
            // Tournament name
            AppTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = stringResource(R.string.field_tournament_name),
                placeholder = stringResource(R.string.hint_tournament_name),
                isError = uiState.nameError != null,
                errorMessage = when (uiState.nameError) {
                    "required" -> stringResource(R.string.error_name_empty)
                    "too_long" -> stringResource(R.string.error_name_too_long)
                    else -> ""
                },
                maxLength = 40
            )

            // Match type dropdown
            DropdownField(
                label = stringResource(R.string.field_match_type),
                selected = uiState.matchType,
                options = listOf("T20", "ODI", "Test"),
                onSelected = { viewModel.updateMatchType(it) }
            )

            // Team count
            AppTextField(
                value = uiState.teamCount.toString(),
                onValueChange = { it.toIntOrNull()?.let { n -> viewModel.updateTeamCount(n) } },
                label = stringResource(R.string.field_team_count),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.teamCountError != null,
                errorMessage = if (uiState.teamCountError != null) stringResource(R.string.error_team_count) else "",
                maxLength = 2
            )

            // Point system
            DropdownField(
                label = stringResource(R.string.field_point_system),
                selected = uiState.pointSystem,
                options = listOf("Standard", "Custom"),
                onSelected = { viewModel.updatePointSystem(it) }
            )

            // Structure
            DropdownField(
                label = stringResource(R.string.field_structure),
                selected = uiState.structure,
                options = listOf("Round Robin", "Playoffs", "Mixed"),
                onSelected = { viewModel.updateStructure(it) }
            )

            // Duration
            DropdownField(
                label = stringResource(R.string.field_duration),
                selected = uiState.duration,
                options = listOf("Weekend", "Week", "Month"),
                onSelected = { viewModel.updateDuration(it) }
            )

            // Location
            AppTextField(
                value = uiState.location,
                onValueChange = { viewModel.updateLocation(it) },
                label = stringResource(R.string.field_location),
                placeholder = stringResource(R.string.hint_location),
                maxLength = 100
            )

            // Team names
            Text(
                text = stringResource(R.string.create_tournament_teams_section),
                style = typo.headingMedium,
                color = colors.textPrimary
            )
            uiState.teamNames.forEachIndexed { index, name ->
                AppTextField(
                    value = name,
                    onValueChange = { viewModel.updateTeamName(index, it) },
                    label = "Team ${index + 1}",
                    isError = uiState.teamNameErrors.getOrNull(index) != null,
                    errorMessage = if (uiState.teamNameErrors.getOrNull(index) != null) stringResource(R.string.error_team_name_empty) else "",
                    maxLength = 30
                )
            }

            Spacer(modifier = Modifier.height(dimens.spacingSm))

            AppPrimaryButton(
                text = stringResource(R.string.btn_save),
                onClick = { viewModel.saveTournament() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving
            )

            Spacer(modifier = Modifier.height(dimens.spacingXl))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val shapes = LocalAppTheme.shapes
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label, style = typo.caption) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.divider,
                focusedLabelColor = colors.accent,
                unfocusedLabelColor = colors.textSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, style = typo.bodyMedium) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
