package com.cricketfest.cricketfestivalplanner.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.datastore.UserPreferencesDataStore
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val activeTournaments: List<Tournament> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val tournamentRepository: TournamentRepository,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    val organizerName: StateFlow<String> = preferencesDataStore.organizerName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        loadTournaments()
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            tournamentRepository.getActiveTournaments()
                .catch { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) }
                .collect { tournaments ->
                    _uiState.value = _uiState.value.copy(isLoading = false, activeTournaments = tournaments)
                }
        }
    }
}
