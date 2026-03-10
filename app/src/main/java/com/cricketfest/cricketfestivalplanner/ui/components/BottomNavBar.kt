package com.cricketfest.cricketfestivalplanner.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cricketfest.cricketfestivalplanner.R
import com.cricketfest.cricketfestivalplanner.ui.navigation.Routes
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography

    val items = listOf(
        BottomNavItem(Routes.Home.route, R.string.nav_home, Icons.Default.Home),
        BottomNavItem(Routes.TournamentList.route, R.string.nav_tournaments, Icons.Default.Sports),
        BottomNavItem(Routes.Analytics.route, R.string.nav_analytics, Icons.Default.Analytics),
        BottomNavItem(Routes.Settings.route, R.string.nav_settings, Icons.Default.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = colors.backgroundCard,
        contentColor = colors.textPrimary
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelResId)
                    )
                },
                label = {
                    Text(text = stringResource(item.labelResId), style = typo.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.accent,
                    selectedTextColor = colors.accent,
                    indicatorColor = colors.accent.copy(alpha = 0.12f),
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary
                )
            )
        }
    }
}
