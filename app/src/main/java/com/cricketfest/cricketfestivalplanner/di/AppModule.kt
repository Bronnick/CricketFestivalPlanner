package com.cricketfest.cricketfestivalplanner.di

import androidx.room.Room
import com.cricketfest.cricketfestivalplanner.data.datastore.UserPreferencesDataStore
import com.cricketfest.cricketfestivalplanner.utils.DataBackupManager
import com.cricketfest.cricketfestivalplanner.data.db.AppDatabase
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepository
import com.cricketfest.cricketfestivalplanner.data.repository.MatchRepositoryImpl
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepository
import com.cricketfest.cricketfestivalplanner.data.repository.TournamentRepositoryImpl
import com.cricketfest.cricketfestivalplanner.ui.screens.analytics.AnalyticsViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.home.HomeViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.match.result.MatchResultViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.match.schedule.MatchScheduleViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.onboarding.OnboardingViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.preloader.PreloaderViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.settings.SettingsViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.table.TournamentTableViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.create.CreateTournamentViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.detail.TournamentDetailViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.edit.EditTournamentViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.history.TournamentHistoryViewModel
import com.cricketfest.cricketfestivalplanner.ui.screens.tournament.list.TournamentListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "cricket_festival_db"
        ).build()
    }

    // DAOs
    single { get<AppDatabase>().tournamentDao() }
    single { get<AppDatabase>().teamDao() }
    single { get<AppDatabase>().matchDao() }
    single { get<AppDatabase>().matchResultDao() }

    // DataStore
    single { UserPreferencesDataStore(androidContext()) }

    // Backup
    single { DataBackupManager(androidContext(), get(), get(), get(), get()) }

    // Repositories
    single<TournamentRepository> { TournamentRepositoryImpl(get(), get()) }
    single<MatchRepository> { MatchRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { PreloaderViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TournamentListViewModel(get()) }
    viewModel { (tournamentId: Long) -> TournamentDetailViewModel(get(), tournamentId) }
    viewModel { CreateTournamentViewModel(get(), get()) }
    viewModel { (tournamentId: Long) -> EditTournamentViewModel(get(), get(), tournamentId) }
    viewModel { (tournamentId: Long) -> MatchScheduleViewModel(get(), tournamentId) }
    viewModel { (matchId: Long) -> MatchResultViewModel(get(), get(), matchId) }
    viewModel { (tournamentId: Long) -> TournamentTableViewModel(get(), tournamentId) }
    viewModel { AnalyticsViewModel(get(), get()) }
    viewModel { TournamentHistoryViewModel(get()) }
    viewModel { SettingsViewModel(get(), get<DataBackupManager>()) }
}
