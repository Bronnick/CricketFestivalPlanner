package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TournamentDetailUiState(
    val isLoading: Boolean = true,
    val tournament: Tournament? = null,
    val showCompleteDialog: Boolean = false,
    val completed: Boolean = false,
    val error: String? = null
)

class TournamentDetailViewModel(
    private val tournamentRepository: TournamentRepository,
    private val tournamentId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(TournamentDetailUiState())
    val uiState: StateFlow<TournamentDetailUiState> = _uiState

    init {
        loadTournament()
    }

    fun loadTournament() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val tournament = tournamentRepository.getTournamentById(tournamentId)
            if (tournament != null) {
                _uiState.value = _uiState.value.copy(isLoading = false, tournament = tournament)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Tournament not found")
            }
        }
    }

    fun showCompleteDialog() {
        _uiState.value = _uiState.value.copy(showCompleteDialog = true)
    }

    fun dismissCompleteDialog() {
        _uiState.value = _uiState.value.copy(showCompleteDialog = false)
    }

    fun markAsCompleted() {
        val tournament = _uiState.value.tournament ?: return
        viewModelScope.launch {
            tournamentRepository.updateTournament(tournament.copy(isCompleted = true))
            _uiState.value = _uiState.value.copy(showCompleteDialog = false, completed = true)
        }
    }
}
