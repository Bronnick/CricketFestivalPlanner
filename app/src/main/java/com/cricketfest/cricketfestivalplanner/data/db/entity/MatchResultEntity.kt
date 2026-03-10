package com.cricketfest.cricketfestivalplanner.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_results")
data class MatchResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val homeScore: Int,
    val awayScore: Int,
    val bestPlayer: String = ""
)
