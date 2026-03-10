package com.cricketfest.cricketfestivalplanner.data.db.dao

import androidx.room.*
import com.cricketfest.cricketfestivalplanner.data.db.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams WHERE tournamentId = :tournamentId ORDER BY id ASC")
    fun getTeamsByTournament(tournamentId: Long): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE tournamentId = :tournamentId ORDER BY id ASC")
    suspend fun getTeamsByTournamentOnce(tournamentId: Long): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Query("DELETE FROM teams WHERE tournamentId = :tournamentId")
    suspend fun deleteTeamsByTournament(tournamentId: Long)

    @Query("SELECT * FROM teams ORDER BY id ASC")
    suspend fun getAllTeamsOnce(): List<TeamEntity>

    @Query("DELETE FROM teams")
    suspend fun deleteAll()
}
