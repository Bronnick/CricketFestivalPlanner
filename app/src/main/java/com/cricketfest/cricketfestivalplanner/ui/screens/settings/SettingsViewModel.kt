package com.cricketfest.cricketfestivalplanner.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.datastore.UserPreferencesDataStore
import com.cricketfest.cricketfestivalplanner.utils.DataBackupManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLoading: Boolean = false,
    val showClearDataDialog: Boolean = false,
    val message: String? = null,
    val navigateToHome: Boolean = false
)

class SettingsViewModel(
    private val preferencesDataStore: UserPreferencesDataStore,
    private val backupManager: DataBackupManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    val organizerName: StateFlow<String> = preferencesDataStore.organizerName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val selectedTheme: StateFlow<String> = preferencesDataStore.selectedTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Light")

    fun updateOrganizerName(name: String) {
        viewModelScope.launch {
            preferencesDataStore.setOrganizerName(name)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            preferencesDataStore.setTheme(theme)
        }
    }

    fun showClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = true)
    }

    fun dismissClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = false)
    }

    fun clearAllData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, showClearDataDialog = false)
            backupManager.clearAll()
            preferencesDataStore.clearAll()
            _uiState.value = _uiState.value.copy(isLoading = false, message = "clear_success")
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            preferencesDataStore.setTheme("Light")
            preferencesDataStore.setOrganizerName("")
            _uiState.value = _uiState.value.copy(message = "reset_success")
        }
    }

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = backupManager.exportToUri(uri)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = if (result.isSuccess) "export_success" else "export_error"
            )
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = backupManager.importFromUri(uri)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = if (result.isSuccess) "import_success" else "import_error",
                navigateToHome = result.isSuccess
            )
        }
    }

    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun dismissNavigateToHome() {
        _uiState.value = _uiState.value.copy(navigateToHome = false)
    }
}
