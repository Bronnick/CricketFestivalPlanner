package com.cricketfest.cricketfestivalplanner.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tournamentId: Long,
    val homeTeamId: Long,
    val awayTeamId: Long,
    val homeTeamName: String = "",
    val awayTeamName: String = "",
    val scheduledTime: Long,
    val isCompleted: Boolean = false,
    val round: Int = 1
)
