package com.cricketfest.cricketfestivalplanner.ui.screens.tournament.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.model.Team
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import com.cricketfest.cricketfestivalplanner.utils.ScheduleGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateTournamentUiState(
    val name: String = "",
    val matchType: String = "T20",
    val teamCount: Int = 4,
    val pointSystem: String = "Standard",
    val structure: String = "Round Robin",
    val duration: String = "Weekend",
    val location: String = "",
    val teamNames: List<String> = List(4) { "" },
    val nameError: String? = null,
    val teamNameErrors: List<String?> = List(4) { null },
    val teamCountError: String? = null,
    val isSaving: Boolean = false,
    val savedTournamentId: Long? = null,
    val error: String? = null
)

class CreateTournamentViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTournamentUiState())
    val uiState: StateFlow<CreateTournamentUiState> = _uiState

    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(name = value, nameError = null)
    }

    fun updateMatchType(value: String) {
        _uiState.value = _uiState.value.copy(matchType = value)
    }

    fun updateTeamCount(value: Int) {
        val clamped = value.coerceIn(2, 32)
        val currentNames = _uiState.value.teamNames
        val newNames = if (clamped > currentNames.size) {
            currentNames + List(clamped - currentNames.size) { "" }
        } else {
            currentNames.take(clamped)
        }
        _uiState.value = _uiState.value.copy(
            teamCount = clamped,
            teamNames = newNames,
            teamNameErrors = List(clamped) { null },
            teamCountError = null
        )
    }

    fun updatePointSystem(value: String) {
        _uiState.value = _uiState.value.copy(pointSystem = value)
    }

    fun updateStructure(value: String) {
        _uiState.value = _uiState.value.copy(structure = value)
    }

    fun updateDuration(value: String) {
        _uiState.value = _uiState.value.copy(duration = value)
    }

    fun updateLocation(value: String) {
        _uiState.value = _uiState.value.copy(location = value)
    }

    fun updateTeamName(index: Int, value: String) {
        val names = _uiState.value.teamNames.toMutableList()
        names[index] = value
        val errors = _uiState.value.teamNameErrors.toMutableList()
        errors[index] = null
        _uiState.value = _uiState.value.copy(teamNames = names, teamNameErrors = errors)
    }

    private fun validate(): Boolean {
        var valid = true
        val state = _uiState.value
        var nameError: String? = null
        var teamCountError: String? = null
        val teamNameErrors = state.teamNameErrors.toMutableList()

        if (state.name.isBlank()) {
            nameError = "required"
            valid = false
        } else if (state.name.length > 40) {
            nameError = "too_long"
            valid = false
        }

        if (state.teamCount < 2 || state.teamCount > 32) {
            teamCountError = "invalid"
            valid = false
        }

        state.teamNames.forEachIndexed { i, name ->
            if (name.isBlank()) {
                teamNameErrors[i] = "required"
                valid = false
            }
        }

        _uiState.value = state.copy(
            nameError = nameError,
            teamCountError = teamCountError,
            teamNameErrors = teamNameErrors
        )
        return valid
    }

    fun loadTemplate(templateId: Long) {
        viewModelScope.launch {
            val template = tournamentRepository.getTournamentById(templateId) ?: return@launch
            val teams = tournamentRepository.getTeamsByTournamentOnce(templateId)
            _uiState.value = _uiState.value.copy(
                matchType = template.matchType,
                teamCount = teams.size.coerceIn(2, 32),
                pointSystem = template.pointSystem,
                structure = template.structure,
                duration = template.duration,
                teamNames = List(teams.size.coerceIn(2, 32)) { "" },
                teamNameErrors = List(teams.size.coerceIn(2, 32)) { null }
            )
        }
    }

    fun saveTournament() {
        if (!validate()) return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            try {
                val tournament = Tournament(
                    name = state.name,
                    matchType = state.matchType,
                    teamCount = state.teamCount,
                    pointSystem = state.pointSystem,
                    structure = state.structure,
                    duration = state.duration,
                    location = state.location
                )
                val tournamentId = tournamentRepository.insertTournament(tournament)

                val teams = state.teamNames.mapIndexed { _, name ->
                    Team(name = name, tournamentId = tournamentId)
                }
                tournamentRepository.insertTeams(teams)

                val savedTeams = tournamentRepository.getTeamsByTournamentOnce(tournamentId)
                val schedule = ScheduleGenerator.generateSchedule(tournamentId, savedTeams, state.structure)
                matchRepository.insertMatches(schedule)

                _uiState.value = _uiState.value.copy(isSaving = false, savedTournamentId = tournamentId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = e.message)
            }
        }
    }
}
