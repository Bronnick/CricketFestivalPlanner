package com.cricketfest.cricketfestivalplanner.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    Scaffold(
        containerColor = colors.backgroundPage,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings_privacy_policy), style = typo.headingMedium, color = colors.textPrimary) },
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = stringResource(R.string.privacy_policy_intro), style = typo.bodyMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(dimens.spacingMd))

            Text(text = stringResource(R.string.privacy_policy_data_title), style = typo.headingMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            Text(text = stringResource(R.string.privacy_policy_data_body), style = typo.bodyMedium, color = colors.textSecondary)
            Spacer(modifier = Modifier.height(dimens.spacingMd))

            Text(text = stringResource(R.string.privacy_policy_storage_title), style = typo.headingMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            Text(text = stringResource(R.string.privacy_policy_storage_body), style = typo.bodyMedium, color = colors.textSecondary)
            Spacer(modifier = Modifier.height(dimens.spacingMd))

            Text(text = stringResource(R.string.privacy_policy_contact_title), style = typo.headingMedium, color = colors.textPrimary)
            Spacer(modifier = Modifier.height(dimens.spacingSm))
            Text(text = stringResource(R.string.privacy_policy_contact_body), style = typo.bodyMedium, color = colors.textSecondary)

            Spacer(modifier = Modifier.height(dimens.spacingXl))
        }
    }
}
