package com.cricketfest.cricketfestivalplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cricketfest.cricketfestivalplanner.ui.screens.analytics.AnalyticsScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.home.HomeScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.match.result.MatchResultScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.match.schedule.MatchScheduleScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.onboarding.OnboardingScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.preloader.PreloaderScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.settings.PrivacyPolicyScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.settings.SettingsScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.table.TournamentTableScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.create.CreateTournamentScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.detail.TournamentDetailScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.edit.EditTournamentScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.history.TournamentHistoryScreen
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.list.TournamentListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Preloader.route
    ) {
        composable(Routes.Preloader.route) {
            PreloaderScreen(navController = navController)
        }
        composable(Routes.Onboarding1.route) {
            OnboardingScreen(page = 1, navController = navController)
        }
        composable(Routes.Onboarding2.route) {
            OnboardingScreen(page = 2, navController = navController)
        }
        composable(Routes.Onboarding3.route) {
            OnboardingScreen(page = 3, navController = navController)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Routes.TournamentList.route) {
            TournamentListScreen(navController = navController)
        }
        composable(Routes.Analytics.route) {
            AnalyticsScreen(navController = navController)
        }
        composable(Routes.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Routes.PrivacyPolicy.route) {
            PrivacyPolicyScreen(navController = navController)
        }
        composable(Routes.TournamentHistory.route) {
            TournamentHistoryScreen(navController = navController)
        }
        composable(Routes.CreateTournament.route) {
            CreateTournamentScreen(navController = navController)
        }
        composable(
            route = Routes.CreateTournamentFromTemplate.route,
            arguments = listOf(navArgument(Routes.CreateTournamentFromTemplate.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getLong(Routes.CreateTournamentFromTemplate.ARG) ?: return@composable
            CreateTournamentScreen(navController = navController, templateId = templateId)
        }
        composable(
            route = Routes.TournamentDetail.route,
            arguments = listOf(navArgument(Routes.TournamentDetail.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.TournamentDetail.ARG) ?: return@composable
            TournamentDetailScreen(tournamentId = id, navController = navController)
        }
        composable(
            route = Routes.EditTournament.route,
            arguments = listOf(navArgument(Routes.EditTournament.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.EditTournament.ARG) ?: return@composable
            EditTournamentScreen(tournamentId = id, navController = navController)
        }
        composable(
            route = Routes.MatchSchedule.route,
            arguments = listOf(navArgument(Routes.MatchSchedule.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.MatchSchedule.ARG) ?: return@composable
            MatchScheduleScreen(tournamentId = id, navController = navController)
        }
        composable(
            route = Routes.MatchResult.route,
            arguments = listOf(navArgument(Routes.MatchResult.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.MatchResult.ARG) ?: return@composable
            MatchResultScreen(matchId = id, navController = navController)
        }
        composable(
            route = Routes.TournamentTable.route,
            arguments = listOf(navArgument(Routes.TournamentTable.ARG) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.TournamentTable.ARG) ?: return@composable
            TournamentTableScreen(tournamentId = id, navController = navController)
        }
    }
}
