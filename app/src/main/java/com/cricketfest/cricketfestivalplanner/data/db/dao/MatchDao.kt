package com.cricketfest.cricketfestivalplanner.data.db.dao

import androidx.room.*
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId ORDER BY round ASC, scheduledTime ASC")
    fun getMatchesByTournament(tournamentId: Long): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getMatchById(id: Long): MatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE tournamentId = :tournamentId")
    suspend fun deleteMatchesByTournament(tournamentId: Long)

    @Query("SELECT * FROM matches ORDER BY id ASC")
    suspend fun getAllMatchesOnce(): List<MatchEntity>

    @Query("DELETE FROM matches")
    suspend fun deleteAll()
}
