package com.cricketfest.cricketfestivalplanner.data.repository

import com.cricketfest.cricketfestivalplanner.data.model.Team
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import kotlinx.coroutines.flow.Flow

interface TournamentRepository {
    fun getAllTournaments(): Flow<List<Tournament>>
    fun getActiveTournaments(): Flow<List<Tournament>>
    fun getCompletedTournaments(): Flow<List<Tournament>>
    suspend fun getTournamentById(id: Long): Tournament?
    suspend fun insertTournament(tournament: Tournament): Long
    suspend fun updateTournament(tournament: Tournament)
    suspend fun deleteTournament(id: Long)
    fun getTeamsByTournament(tournamentId: Long): Flow<List<Team>>
    suspend fun getTeamsByTournamentOnce(tournamentId: Long): List<Team>
    suspend fun insertTeams(teams: List<Team>)
    suspend fun deleteTeamsByTournament(tournamentId: Long)
}
