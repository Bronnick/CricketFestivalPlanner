package com.cricketfest.cricketfestivalplanner.data.db.dao

import androidx.room.*
import com.cricketfest.cricketfestivalplanner.data.db.entity.TournamentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments ORDER BY createdAt DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getTournamentById(id: Long): TournamentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity): Long

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    @Query("DELETE FROM tournaments WHERE id = :id")
    suspend fun deleteTournament(id: Long)

    @Query("SELECT * FROM tournaments ORDER BY createdAt ASC")
    suspend fun getAllTournamentsOnce(): List<TournamentEntity>

    @Query("DELETE FROM tournaments")
    suspend fun deleteAll()
}
