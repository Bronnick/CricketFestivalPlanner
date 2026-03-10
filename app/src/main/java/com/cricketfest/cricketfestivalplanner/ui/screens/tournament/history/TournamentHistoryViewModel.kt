package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class TournamentHistoryUiState(
    val isLoading: Boolean = true,
    val tournaments: List<Tournament> = emptyList(),
    val error: String? = null
)

class TournamentHistoryViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TournamentHistoryUiState())
    val uiState: StateFlow<TournamentHistoryUiState> = _uiState

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            tournamentRepository.getCompletedTournaments()
                .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
                .collect { list ->
                    _uiState.value = _uiState.value.copy(isLoading = false, tournaments = list)
                }
        }
    }
}
