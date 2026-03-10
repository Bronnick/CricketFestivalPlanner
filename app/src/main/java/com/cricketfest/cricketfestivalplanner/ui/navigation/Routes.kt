package com.cricketfest.cricketfestivalplanner.ui.navigation

sealed class Routes(val route: String) {
    object Preloader : Routes("preloader")
    object Onboarding1 : Routes("onboarding1")
    object Onboarding2 : Routes("onboarding2")
    object Onboarding3 : Routes("onboarding3")
    object Home : Routes("home")
    object TournamentList : Routes("tournament_list")
    object Analytics : Routes("analytics")
    object Settings : Routes("settings")
    object TournamentHistory : Routes("tournament_history")
    object CreateTournament : Routes("create_tournament")

    object TournamentDetail : Routes("tournament_detail/{tournamentId}") {
        fun createRoute(id: Long) = "tournament_detail/$id"
        const val ARG = "tournamentId"
    }

    object EditTournament : Routes("edit_tournament/{tournamentId}") {
        fun createRoute(id: Long) = "edit_tournament/$id"
        const val ARG = "tournamentId"
    }

    object MatchSchedule : Routes("match_schedule/{tournamentId}") {
        fun createRoute(id: Long) = "match_schedule/$id"
        const val ARG = "tournamentId"
    }

    object MatchResult : Routes("match_result/{matchId}") {
        fun createRoute(id: Long) = "match_result/$id"
        const val ARG = "matchId"
    }

    object TournamentTable : Routes("tournament_table/{tournamentId}") {
        fun createRoute(id: Long) = "tournament_table/$id"
        const val ARG = "tournamentId"
    }

    object PrivacyPolicy : Routes("privacy_policy")

    object CreateTournamentFromTemplate : Routes("create_tournament_from_template/{templateId}") {
        fun createRoute(id: Long) = "create_tournament_from_template/$id"
        const val ARG = "templateId"
    }
}
