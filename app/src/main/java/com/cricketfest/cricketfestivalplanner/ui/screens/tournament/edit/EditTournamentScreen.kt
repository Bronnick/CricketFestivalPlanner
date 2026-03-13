package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
fun EditTournamentScreen(tournamentId: Long, navController: NavController) {
    val viewModel: EditTournamentViewModel = koinViewModel(parameters = { parametersOf(tournamentId) })
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
                title = { Text(text = stringResource(R.string.edit_tournament_title), style = typo.headingMedium, color = colors.textPrimary) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPage)
                    .padding(paddingValues)
                    .padding(dimens.spacingMd)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimens.spacingMd)
            ) {
                AppTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = stringResource(R.string.field_tournament_name),
                    isError = uiState.nameError != null,
                    errorMessage = if (uiState.nameError != null) stringResource(R.string.error_name_empty) else "",
                    maxLength = 40
                )

                EditDropdownField(
                    label = stringResource(R.string.field_match_type),
                    selected = uiState.matchType,
                    options = listOf("T20", "ODI", "Test"),
                    onSelected = { viewModel.updateMatchType(it) }
                )
                EditDropdownField(
                    label = stringResource(R.string.field_point_system),
                    selected = uiState.pointSystem,
                    options = listOf("Standard", "Custom"),
                    onSelected = { viewModel.updatePointSystem(it) }
                )
                EditDropdownField(
                    label = stringResource(R.string.field_structure),
                    selected = uiState.structure,
                    options = listOf("Round Robin", "Playoffs", "Mixed"),
                    onSelected = { viewModel.updateStructure(it) }
                )
                EditDropdownField(
                    label = stringResource(R.string.field_duration),
                    selected = uiState.duration,
                    options = listOf("Weekend", "Week", "Month"),
                    onSelected = { viewModel.updateDuration(it) }
                )
                AppTextField(
                    value = uiState.location,
                    onValueChange = { viewModel.updateLocation(it) },
                    label = stringResource(R.string.field_location),
                    maxLength = 100
                )

                Spacer(modifier = Modifier.height(dimens.spacingSm))
                AppPrimaryButton(
                    text = stringResource(R.string.btn_save),
                    onClick = { viewModel.saveChanges() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving
                )
                Spacer(modifier = Modifier.height(dimens.spacingXl))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDropdownField(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val shapes = LocalAppTheme.shapes
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label, style = typo.caption) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.divider
            ),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, style = typo.bodyMedium) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
        }
    }
}
