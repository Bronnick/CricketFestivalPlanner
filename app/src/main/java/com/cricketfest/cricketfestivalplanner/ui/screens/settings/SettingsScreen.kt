package com.cricketfest.cricketfestivalplanner.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.BuildConfig
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppCard
import com.cricketfest.cricketfestivalplanner.ui.components.AppLoader
import com.cricketfest.cricketfestivalplanner.ui.components.AppTextField
import com.cricketfest.cricketfestivalplanner.ui.components.BottomNavBar
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val organizerName by viewModel.organizerName.collectAsState()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var nameInput by rememberSaveable(organizerName) { mutableStateOf(organizerName) }

    val clearSuccessMsg = stringResource(R.string.settings_clear_data_success)
    val exportSuccessMsg = stringResource(R.string.settings_export_success)
    val importSuccessMsg = stringResource(R.string.settings_import_success)
    val exportErrorMsg = stringResource(R.string.settings_export_error)
    val importErrorMsg = stringResource(R.string.settings_import_error)

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { viewModel.exportData(it) }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.importData(it) }
    }

    LaunchedEffect(uiState.message) {
        val msg = when (uiState.message) {
            "clear_success" -> clearSuccessMsg
            "export_success" -> exportSuccessMsg
            "import_success" -> importSuccessMsg
            "export_error" -> exportErrorMsg
            "import_error" -> importErrorMsg
            else -> null
        }
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissMessage()
        }
    }

    if (uiState.showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissClearDataDialog() },
            title = { Text(text = stringResource(R.string.settings_clear_data), style = typo.headingMedium) },
            text = { Text(text = stringResource(R.string.settings_clear_data_confirm), style = typo.bodyMedium) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearAllData() }) {
                    Text(text = stringResource(R.string.dialog_confirm), color = colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissClearDataDialog() }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = colors.textSecondary)
                }
            },
            containerColor = colors.backgroundCard
        )
    }

    Scaffold(
        containerColor = colors.backgroundPage,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            AppLoader()
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPage)
                .padding(paddingValues)
                .padding(dimens.spacingMd)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = typo.headingLarge,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(dimens.spacingLg))

            // Profile section
            Text(
                text = stringResource(R.string.settings_section_general),
                style = typo.label,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            AppCard {
                AppTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = stringResource(R.string.settings_organizer_name),
                    placeholder = stringResource(R.string.settings_organizer_name_hint),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimens.spacingXs))
                TextButton(
                    onClick = { viewModel.updateOrganizerName(nameInput.trim()) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.btn_save),
                        style = typo.bodyLarge,
                        color = colors.accent,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.spacingLg))

            // Data section
            Text(
                text = stringResource(R.string.settings_section_data),
                style = typo.label,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            AppCard {
                SettingsItem(
                    title = stringResource(R.string.settings_export_data),
                    onClick = {
                        exportLauncher.launch("cricket_backup_${System.currentTimeMillis()}.json")
                    }
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_import_data),
                    onClick = {
                        importLauncher.launch(arrayOf("application/json", "*/*"))
                    }
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_clear_data),
                    onClick = { viewModel.showClearDataDialog() },
                    isDestructive = true
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_reset_settings),
                    onClick = { viewModel.resetSettings() }
                )
            }

            Spacer(modifier = Modifier.height(dimens.spacingLg))

            // About section
            Text(
                text = stringResource(R.string.settings_section_about),
                style = typo.label,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            AppCard {
                SettingsItem(
                    title = stringResource(R.string.settings_rate_app),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"))
                        context.startActivity(intent)
                    }
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_share_app),
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_text))
                        }
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    }
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_privacy_policy),
                    onClick = { navController.navigate(Routes.PrivacyPolicy.route) }
                )
                Divider(color = colors.divider)
                SettingsItem(
                    title = stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(dimens.spacingXl))
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.spacingXs)
    ) {
        Text(
            text = title,
            style = typo.bodyLarge,
            color = if (isDestructive) colors.error else colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
