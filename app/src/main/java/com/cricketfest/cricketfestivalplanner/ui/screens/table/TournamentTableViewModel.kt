package com.cricketfest.cricketfestivalplanner.ui.screens.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.StandingsEntry
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class TournamentTableUiState(
    val isLoading: Boolean = true,
    val standings: List<StandingsEntry> = emptyList(),
    val error: String? = null
)

class TournamentTableViewModel(
    private val matchRepository: MatchRepository,
    private val tournamentId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(TournamentTableUiState())
    val uiState: StateFlow<TournamentTableUiState> = _uiState

    init {
        loadStandings()
    }

    fun loadStandings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            matchRepository.getStandings(tournamentId)
                .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
                .collect { standings ->
                    _uiState.value = _uiState.value.copy(isLoading = false, standings = standings)
                }
        }
    }
}
