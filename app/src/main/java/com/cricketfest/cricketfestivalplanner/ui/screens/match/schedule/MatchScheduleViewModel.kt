package com.cricketfest.cricketfestivalplanner.ui.screens.match.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class MatchScheduleUiState(
    val isLoading: Boolean = true,
    val matches: List<Match> = emptyList(),
    val error: String? = null
)

class MatchScheduleViewModel(
    private val matchRepository: MatchRepository,
    private val tournamentId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchScheduleUiState())
    val uiState: StateFlow<MatchScheduleUiState> = _uiState

    init {
        loadMatches()
    }

    fun loadMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            matchRepository.getMatchesByTournament(tournamentId)
                .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
                .collect { matches ->
                    _uiState.value = _uiState.value.copy(isLoading = false, matches = matches)
                }
        }
    }
}
