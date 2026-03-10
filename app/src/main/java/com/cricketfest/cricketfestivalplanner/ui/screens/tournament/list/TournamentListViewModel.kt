package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

enum class TournamentFilter { ALL, ACTIVE, COMPLETED }

data class TournamentListUiState(
    val isLoading: Boolean = true,
    val tournaments: List<Tournament> = emptyList(),
    val filter: TournamentFilter = TournamentFilter.ALL,
    val error: String? = null
)

class TournamentListViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TournamentListUiState())
    val uiState: StateFlow<TournamentListUiState> = _uiState

    init {
        loadTournaments()
    }

    fun setFilter(filter: TournamentFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadTournaments()
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val flow = when (_uiState.value.filter) {
                TournamentFilter.ACTIVE -> tournamentRepository.getActiveTournaments()
                TournamentFilter.COMPLETED -> tournamentRepository.getCompletedTournaments()
                TournamentFilter.ALL -> tournamentRepository.getAllTournaments()
            }
            flow.catch { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }.collect { list ->
                _uiState.value = _uiState.value.copy(isLoading = false, tournaments = list)
            }
        }
    }
}
