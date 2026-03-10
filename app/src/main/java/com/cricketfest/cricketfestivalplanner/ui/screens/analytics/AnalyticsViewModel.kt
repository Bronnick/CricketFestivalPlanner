package com.cricketfest.cricketfestivalplanner.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TournamentStats(
    val tournament: Tournament,
    val matchesPlayed: Int,
    val totalMatches: Int,
    val topTeam: String
)

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val totalTournaments: Int = 0,
    val totalMatchesPlayed: Int = 0,
    val topTeam: String = "",
    val tournamentStats: List<TournamentStats> = emptyList(),
    val error: String? = null
)

class AnalyticsViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            tournamentRepository.getAllTournaments()
                .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
                .collect { tournaments ->
                    computeStats(tournaments)
                }
        }
    }

    private suspend fun computeStats(tournaments: List<Tournament>) {
        val allMatches = matchRepository.getAllMatchesOnce()
        val allResults = matchRepository.getAllResultsOnce()
        val resultMap = allResults.associateBy { it.matchId }

        val totalMatchesPlayed = allMatches.count { it.isCompleted }

        // wins per team name across all tournaments
        val winsPerTeam = mutableMapOf<String, Int>()
        allMatches.filter { it.isCompleted }.forEach { match ->
            val result = resultMap[match.id] ?: return@forEach
            val winner = when {
                result.homeScore > result.awayScore -> match.homeTeamName
                result.awayScore > result.homeScore -> match.awayTeamName
                else -> null
            }
            if (winner != null) winsPerTeam[winner] = (winsPerTeam[winner] ?: 0) + 1
        }
        val topTeam = winsPerTeam.maxByOrNull { it.value }?.key ?: ""

        val matchesByTournament = allMatches.groupBy { it.tournamentId }
        val tournamentStats = tournaments.map { t ->
            val tMatches = matchesByTournament[t.id] ?: emptyList()
            val played = tMatches.count { it.isCompleted }
            val teamWins = mutableMapOf<String, Int>()
            tMatches.filter { it.isCompleted }.forEach { match ->
                val result = resultMap[match.id] ?: return@forEach
                val winner = when {
                    result.homeScore > result.awayScore -> match.homeTeamName
                    result.awayScore > result.homeScore -> match.awayTeamName
                    else -> null
                }
                if (winner != null) teamWins[winner] = (teamWins[winner] ?: 0) + 1
            }
            val tournamentTopTeam = teamWins.maxByOrNull { it.value }?.key ?: ""
            TournamentStats(t, played, tMatches.size, tournamentTopTeam)
        }

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            totalTournaments = tournaments.size,
            totalMatchesPlayed = totalMatchesPlayed,
            topTeam = topTeam,
            tournamentStats = tournamentStats
        )
    }
}
