package com.cricketfest.cricketfestivalplanner.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricketfest.cricketfestivalplanner.data.datastore.UserPreferencesDataStore
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesDataStore.setOnboardingCompleted(true)
        }
    }
}
