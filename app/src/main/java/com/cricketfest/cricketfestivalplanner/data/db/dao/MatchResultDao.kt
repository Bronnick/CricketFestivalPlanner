package com.cricketfest.cricketfestivalplanner.data.db.dao

import androidx.room.*
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchResultDao {
    @Query("SELECT * FROM match_results WHERE matchId = :matchId LIMIT 1")
    suspend fun getResultByMatchId(matchId: Long): MatchResultEntity?

    @Query("SELECT * FROM match_results WHERE matchId IN (SELECT id FROM matches WHERE tournamentId = :tournamentId)")
    fun getResultsByTournament(tournamentId: Long): Flow<List<MatchResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateResult(result: MatchResultEntity)

    @Query("SELECT * FROM match_results ORDER BY id ASC")
    suspend fun getAllResultsOnce(): List<MatchResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<MatchResultEntity>)

    @Query("DELETE FROM match_results")
    suspend fun deleteAll()
}
