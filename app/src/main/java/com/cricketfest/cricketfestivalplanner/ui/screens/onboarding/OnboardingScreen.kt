package com.cricketfest.cricketfestivalplanner.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.components.AppOutlinedButton
import com.cricketfest.cricketfestivalplanner.ui.components.AppPrimaryButton
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(page: Int, navController: NavController) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val dimens = LocalAppTheme.dimens

    val titleRes = when (page) {
        1 -> R.string.onboarding_1_title
        2 -> R.string.onboarding_2_title
        else -> R.string.onboarding_3_title
    }
    val subtitleRes = when (page) {
        1 -> R.string.onboarding_1_subtitle
        2 -> R.string.onboarding_2_subtitle
        else -> R.string.onboarding_3_subtitle
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPage)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spacingXl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(dimens.spacingXl))

            // Illustration placeholder
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(colors.accent.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏏",
                    style = typo.headingLarge.copy(fontSize = androidx.compose.ui.unit.TextUnit(64f, androidx.compose.ui.unit.TextUnitType.Sp))
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(titleRes),
                    style = typo.headingLarge,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(dimens.spacingMd))
                Text(
                    text = stringResource(subtitleRes),
                    style = typo.bodyLarge,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Page indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = dimens.spacingLg)
                ) {
                    (1..3).forEach { dot ->
                        Box(
                            modifier = Modifier
                                .size(if (dot == page) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (dot == page) colors.accent else colors.divider)
                        )
                    }
                }

                if (page < 3) {
                    AppPrimaryButton(
                        text = stringResource(R.string.btn_next),
                        onClick = {
                            navController.navigate(
                                when (page) {
                                    1 -> Routes.Onboarding2.route
                                    else -> Routes.Onboarding3.route
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (page > 1) {
                        Spacer(modifier = Modifier.height(dimens.spacingSm))
                        AppOutlinedButton(
                            text = stringResource(R.string.btn_back),
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    AppPrimaryButton(
                        text = stringResource(R.string.btn_finish),
                        onClick = {
                            viewModel.completeOnboarding()
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Onboarding1.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(dimens.spacingSm))
                    AppOutlinedButton(
                        text = stringResource(R.string.btn_back),
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(dimens.spacingMd))
            }
        }
    }
}
