package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Team
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditTournamentUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val matchType: String = "T20",
    val teamCount: Int = 4,
    val pointSystem: String = "Standard",
    val structure: String = "Round Robin",
    val duration: String = "Weekend",
    val location: String = "",
    val teamNames: List<String> = emptyList(),
    val nameError: String? = null,
    val teamNameErrors: List<String?> = emptyList(),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class EditTournamentViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository,
    private val tournamentId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTournamentUiState())
    val uiState: StateFlow<EditTournamentUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val tournament = tournamentRepository.getTournamentById(tournamentId) ?: return@launch
            val teams = tournamentRepository.getTeamsByTournamentOnce(tournamentId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                name = tournament.name,
                matchType = tournament.matchType,
                teamCount = tournament.teamCount,
                pointSystem = tournament.pointSystem,
                structure = tournament.structure,
                duration = tournament.duration,
                location = tournament.location,
                teamNames = teams.map { it.name },
                teamNameErrors = List(teams.size) { null }
            )
        }
    }

    fun updateName(value: String) { _uiState.value = _uiState.value.copy(name = value, nameError = null) }
    fun updateMatchType(value: String) { _uiState.value = _uiState.value.copy(matchType = value) }
    fun updatePointSystem(value: String) { _uiState.value = _uiState.value.copy(pointSystem = value) }
    fun updateStructure(value: String) { _uiState.value = _uiState.value.copy(structure = value) }
    fun updateDuration(value: String) { _uiState.value = _uiState.value.copy(duration = value) }
    fun updateLocation(value: String) { _uiState.value = _uiState.value.copy(location = value) }
    fun updateTeamName(index: Int, value: String) {
        val names = _uiState.value.teamNames.toMutableList()
        names[index] = value
        _uiState.value = _uiState.value.copy(teamNames = names)
    }

    fun saveChanges() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.value = state.copy(nameError = "required")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            try {
                val existing = tournamentRepository.getTournamentById(tournamentId) ?: return@launch
                tournamentRepository.updateTournament(
                    existing.copy(
                        name = state.name,
                        matchType = state.matchType,
                        pointSystem = state.pointSystem,
                        structure = state.structure,
                        duration = state.duration,
                        location = state.location
                    )
                )
                _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }
}
