package com.cricketfest.cricketfestivalplanner.ui.screens.preloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class PreloaderDestination {
    object Loading : PreloaderDestination()
    object Onboarding : PreloaderDestination()
    object Home : PreloaderDestination()
}

class PreloaderViewModel(
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _destination = MutableStateFlow<PreloaderDestination>(PreloaderDestination.Loading)
    val destination: StateFlow<PreloaderDestination> = _destination

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            // Simulate initialization delay
            kotlinx.coroutines.delay(1500)
            val completed = preferencesDataStore.isOnboardingCompleted.first()
            _destination.value = if (completed) PreloaderDestination.Home else PreloaderDestination.Onboarding
        }
    }
}
