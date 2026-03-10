package com.cricketfest.cricketfestivalplanner.ui.screens.match.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.data.model.MatchResult
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MatchResultUiState(
    val isLoading: Boolean = true,
    val match: Match? = null,
    val homeScore: String = "",
    val awayScore: String = "",
    val bestPlayer: String = "",
    val homeScoreError: Boolean = false,
    val awayScoreError: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class MatchResultViewModel(
    private val matchRepository: MatchRepository,
    private val tournamentRepository: TournamentRepository,
    private val matchId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchResultUiState())
    val uiState: StateFlow<MatchResultUiState> = _uiState

    init {
        loadMatch()
    }

    private fun loadMatch() {
        viewModelScope.launch {
            val match = matchRepository.getMatchById(matchId)
            val existing = matchRepository.getMatchResult(matchId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                match = match,
                homeScore = existing?.homeScore?.toString() ?: "",
                awayScore = existing?.awayScore?.toString() ?: "",
                bestPlayer = existing?.bestPlayer ?: ""
            )
        }
    }

    fun updateHomeScore(value: String) {
        _uiState.value = _uiState.value.copy(homeScore = value, homeScoreError = false)
    }

    fun updateAwayScore(value: String) {
        _uiState.value = _uiState.value.copy(awayScore = value, awayScoreError = false)
    }

    fun updateBestPlayer(value: String) {
        _uiState.value = _uiState.value.copy(bestPlayer = value)
    }

    fun saveResult() {
        val state = _uiState.value
        val homeScore = state.homeScore.toIntOrNull()
        val awayScore = state.awayScore.toIntOrNull()

        if (homeScore == null || homeScore < 0) {
            _uiState.value = state.copy(homeScoreError = true)
            return
        }
        if (awayScore == null || awayScore < 0) {
            _uiState.value = state.copy(awayScoreError = true)
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            try {
                matchRepository.insertOrUpdateResult(
                    MatchResult(
                        matchId = matchId,
                        homeScore = homeScore,
                        awayScore = awayScore,
                        bestPlayer = state.bestPlayer
                    )
                )
                val match = state.match
                if (match != null) {
                    matchRepository.updateMatch(match.copy(isCompleted = true))
                }
                _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }
}
